package com.expensetracker.userservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response format")
public class ErrorResponse {

  @Schema(
      description = "Timestamp when the error occurred",
      example = "2025-05-11T12:34:56.789",
      type = "string",
      format = "date-time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  private LocalDateTime timestamp;

  @Schema(description = "HTTP status code", example = "400", type = "integer")
  private int status;

  @Schema(description = "Error type", example = "Bad Request", type = "string")
  private String error;

  @Schema(description = "Error message", example = "Input validation failed", type = "string")
  private String message;

  @Schema(description = "Request path", example = "/api/v1/users/login", type = "string")
  private String path;

  @ArraySchema(
      arraySchema =
          @Schema(description = "Detailed validation errors (only present for validation errors)"),
      schema = @Schema(type = "string", example = "email: Email should be valid"))
  private List<String> details;
}
