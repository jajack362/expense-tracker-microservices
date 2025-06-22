package com.expensetracker.userservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating a new user")
public class CreateUserRequest {

  @Schema(description = "Username for the new account", example = "jack12", required = true)
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @Schema(
      description = "Email address for the new account",
      example = "jack12@email.com",
      required = true)
  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @Schema(
      description =
          "Password for the new account (must contain at least one digit, one lowercase, one uppercase, and one special character)",
      example = "SecureP@ssw0rd",
      required = true)
  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 50, message = "Password must be between 3 and 50 characters")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
      message =
          "Password must contain at least one digit, one lowercase, one uppercase, and one special character")
  private String password;
}
