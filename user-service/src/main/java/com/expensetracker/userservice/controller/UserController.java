package com.expensetracker.userservice.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expensetracker.userservice.dto.*;
import com.expensetracker.userservice.dto.request.CreateUserRequest;
import com.expensetracker.userservice.dto.request.LoginRequest;
import com.expensetracker.userservice.dto.response.AuthResponse;
import com.expensetracker.userservice.dto.response.UserResponse;
import com.expensetracker.userservice.service.UserService;
import com.expensetracker.userservice.util.UserMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(
    name = "User Management",
    description = "Operations for user registration, authentication and management")
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @Operation(
      summary = "Get user by ID",
      description = "Retrieve user details by user ID. Requires authentication.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
    @ApiResponse(responseCode = "401", ref = "#/components/responses/UnauthorizedError"),
    @ApiResponse(responseCode = "403", ref = "#/components/responses/ForbiddenError"),
    @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFoundError"),
    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
    UserDTO userDTO = userService.getUserById(id);
    return ResponseEntity.ok(userMapper.toResponse(userDTO));
  }

  @Operation(
      summary = "Register a new user",
      description = "Create a new user account. No authentication required.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "User created successfully"),
    @ApiResponse(responseCode = "400", ref = "#/components/responses/ValidationError"),
    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequestError"),
    @ApiResponse(responseCode = "409", ref = "#/components/responses/ConflictError"),
    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
  })
  @PostMapping("/register")
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
    // Convert request to DTO
    UserDTO userDTO = userMapper.toDTO(request);

    // Call service
    UserDTO createdUser = userService.createUser(userDTO);

    // Convert to response
    UserResponse response = userMapper.toResponse(createdUser);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Operation(
      summary = "User login",
      description = "Authenticate a user and get a JWT token. No authentication required.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Login successful"),
    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequestError"),
    @ApiResponse(responseCode = "401", ref = "#/components/responses/UnauthorizedError"),
    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
  })
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
    // Convert request to DTO
    UserDTO userDTO = userMapper.toDTO(loginRequest);

    // Call service
    AuthResponse authResponse = userService.authenticateUser(userDTO);

    return ResponseEntity.ok(authResponse);
  }
}
