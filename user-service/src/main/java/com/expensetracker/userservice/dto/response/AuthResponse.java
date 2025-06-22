package com.expensetracker.userservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response with JWT token")
public class AuthResponse {

  @Schema(
      description = "JWT token for authentication",
      example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  private String token;

  @Schema(description = "Token type", example = "Bearer")
  private String tokenType = "Bearer";

  @Schema(description = "User details")
  private UserResponse user;

  @Builder
  public AuthResponse(String token, UserResponse user) {
    this.token = token;
    this.tokenType = "Bearer";
    this.user = user;
  }
}
