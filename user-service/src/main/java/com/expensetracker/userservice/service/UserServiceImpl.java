package com.expensetracker.userservice.service;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expensetracker.userservice.dto.UserDTO;
import com.expensetracker.userservice.dto.response.AuthResponse;
import com.expensetracker.userservice.entity.User;
import com.expensetracker.userservice.exception.ResourceAlreadyExistsException;
import com.expensetracker.userservice.exception.ResourceNotFoundException;
import com.expensetracker.userservice.repository.UserRepository;
import com.expensetracker.userservice.util.JwtTokenProvider;
import com.expensetracker.userservice.util.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @CacheEvict(value = "users", key = "#result.id")
  @Override
  public UserDTO createUser(UserDTO userDTO) {
    // Replace two separate queries with one combined query
    if (userRepository.existsByUsernameOrEmail(userDTO.getUsername(), userDTO.getEmail())) {
      // You may want to determine which field caused the conflict
      if (userRepository.existsByEmail(userDTO.getEmail())) {
        throw new ResourceAlreadyExistsException("Email already exists");
      } else if (userRepository.existsByUsername(userDTO.getUsername())) {
        throw new ResourceAlreadyExistsException("Username already exists");
      }
    }

    // Convert DTO to entity
    User user = userMapper.toEntity(userDTO);

    // Encode password
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // Save the user
    User savedUser = userRepository.save(user);

    // Return DTO
    return userMapper.toDTO(savedUser);
  }

  @Cacheable(value = "users", key = "#id")
  @Override
  public UserDTO getUserById(UUID id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    return userMapper.toDTO(user);
  }

  @Override
  public AuthResponse authenticateUser(UserDTO userDTO) {
    // Find user by username only
    User user =
        userRepository
            .findByUsername(userDTO.getUsername())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "User not found with username: " + userDTO.getUsername()));

    // Verify password
    if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
      throw new BadCredentialsException("Invalid password");
    }

    // Generate JWT token
    String token = jwtTokenProvider.generateToken(user);

    // Return authentication response
    return AuthResponse.builder()
        .token(token)
        .user(userMapper.toResponse(userMapper.toDTO(user)))
        .build();
  }
}
