package com.expensetracker.userservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request containing user credentials")
public class LoginRequest {

  @Schema(description = "Username for authentication", example = "jack12", required = true)
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @Schema(description = "User password", example = "SecureP@ssw0rd", required = true)
  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 50, message = "Password must be between 3 and 50 characters")
  private String password;
}
