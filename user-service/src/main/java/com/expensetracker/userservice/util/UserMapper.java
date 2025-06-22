package com.expensetracker.userservice.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.expensetracker.userservice.dto.UserDTO;
import com.expensetracker.userservice.dto.request.CreateUserRequest;
import com.expensetracker.userservice.dto.request.LoginRequest;
import com.expensetracker.userservice.dto.response.UserResponse;
import com.expensetracker.userservice.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  /** Convert CreateUserRequest to UserDTO */
  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  UserDTO toDTO(CreateUserRequest request);

  /** Convert UserDTO to UserResponse */
  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "updatedAt", target = "updatedAt")
  UserResponse toResponse(UserDTO dto);

  /** Convert User entity to UserDTO */
  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "updatedAt", target = "updatedAt")
  UserDTO toDTO(User user);

  /** Convert UserDTO to User entity */
  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "updatedAt", target = "updatedAt")
  User toEntity(UserDTO dto);

  /** Convert LoginRequest to UserDTO */
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  UserDTO toDTO(LoginRequest loginRequest);
}
