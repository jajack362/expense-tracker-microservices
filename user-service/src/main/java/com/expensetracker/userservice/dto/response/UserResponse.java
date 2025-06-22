package com.expensetracker.userservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User information response object")
public class UserResponse {

  @Schema(
      description = "Unique identifier for the user",
      example = "0196bfde-4e25-7061-b4a4-93e5ce8dbcc7")
  private UUID id;

  @Schema(description = "Username for login", example = "jack12")
  private String username;

  @Schema(description = "User's email address", example = "jack12@email.com")
  private String email;

  @Schema(
      description = "Timestamp when the user was created",
      example = "2025-05-11T16:02:19.430573")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private LocalDateTime createdAt;

  @Schema(
      description = "Timestamp when the user was last updated",
      example = "2025-05-11T16:02:19.430573")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private LocalDateTime updatedAt;
}
