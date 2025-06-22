package com.expensetracker.userservice.service;

import java.util.UUID;

import com.expensetracker.userservice.dto.UserDTO;
import com.expensetracker.userservice.dto.response.AuthResponse;

public interface UserService {
  UserDTO getUserById(UUID id);

  UserDTO createUser(UserDTO userDTO);

  AuthResponse authenticateUser(UserDTO userDTO);
}
