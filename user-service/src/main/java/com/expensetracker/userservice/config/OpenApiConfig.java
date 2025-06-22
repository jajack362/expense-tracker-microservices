package com.expensetracker.userservice.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.expensetracker.userservice.dto.response.ErrorResponse;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI userServiceOpenAPI() {
    // Use ModelConverters to extract schema from your Java class
    // This contains the full ErrorResponse schema extracted from your Java class
    Schema errorResponseSchema =
        ModelConverters.getInstance().readAllAsResolvedSchema(ErrorResponse.class).schema;

    return new OpenAPI()
        .info(
            new Info()
                .title("User Service API")
                .description(
                    "API for user management: registration, authentication, and profile management")
                .version("1.0.0")
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0")))
        .components(
            new Components()
                // Register the ErrorResponse schema extracted from the Java class
                .addSchemas("ErrorResponse", errorResponseSchema)
                // Add security scheme
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT token authentication"))
                // Define standard responses using the registered ErrorResponse schema
                .addResponses(
                    "BadRequestError",
                    createErrorResponse(
                        "Bad Request - The request is not well-formed", createBadRequestExample()))
                .addResponses(
                    "ValidationError",
                    createErrorResponse(
                        "Validation Error - The request contains invalid data",
                        createValidationErrorExample()))
                .addResponses(
                    "UnauthorizedError",
                    createErrorResponse(
                        "Unauthorized - Authentication is required", createUnauthorizedExample()))
                .addResponses(
                    "ForbiddenError",
                    createErrorResponse(
                        "Forbidden - No permission to access the resource",
                        createForbiddenExample()))
                .addResponses(
                    "NotFoundError",
                    createErrorResponse(
                        "Not Found - The requested resource does not exist",
                        createNotFoundExample()))
                .addResponses(
                    "ConflictError",
                    createErrorResponse(
                        "Conflict - Resource already exists", createConflictExample()))
                .addResponses(
                    "InternalServerError",
                    createErrorResponse(
                        "Internal Server Error - An unexpected error occurred",
                        createInternalServerErrorExample())));
  }

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("user-service-public").pathsToMatch("/api/v1/**").build();
  }

  private ApiResponse createErrorResponse(String description, Example example) {
    Content content =
        new Content()
            .addMediaType(
                "application/json",
                new MediaType()
                    .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                    .addExamples("default", example));

    return new ApiResponse().description(description).content(content);
  }

  private Example createValidationErrorExample() {
    Map<String, Object> exampleMap = new HashMap<>();
    exampleMap.put("timestamp", "2025-05-11T12:34:56.789");
    exampleMap.put("status", 400);
    exampleMap.put("error", "Validation Error");
    exampleMap.put(
        "message",
        "Input validation failed - please check the request format and field requirements");
    exampleMap.put("path", "/api/v1/users/register");

    List<String> details = new ArrayList<>();
    details.add("username: Username must be between 3 and 50 characters");
    details.add("email: Email should be valid");
    details.add(
        "password: Password must contain at least one digit, one lowercase, one uppercase, and one special character");
    exampleMap.put("details", details);

    return new Example().value(exampleMap);
  }

  private Example createBadRequestExample() {
    Map<String, Object> exampleMap = new HashMap<>();
    exampleMap.put("timestamp", "2025-05-11T12:34:56.789");
    exampleMap.put("status", 400);
    exampleMap.put("error", "Bad Request");
    exampleMap.put("message", "Invalid request format");
    exampleMap.put("path", "/api/v1/users/login");

    return new Example().value(exampleMap);
  }

  private Example createUnauthorizedExample() {
    Map<String, Object> exampleMap = new HashMap<>();
    exampleMap.put("timestamp", "2025-05-11T12:34:56.789");
    exampleMap.put("status", 401);
    exampleMap.put("error", "Unauthorized");
    exampleMap.put("message", "Invalid credentials");
    exampleMap.put("path", "/api/v1/users/profile");

    return new Example().value(exampleMap);
  }

  private Example createForbiddenExample() {
    Map<String, Object> exampleMap = new HashMap<>();
    exampleMap.put("timestamp", "2025-05-11T12:34:56.789");
    exampleMap.put("status", 403);
    exampleMap.put("error", "Forbidden");
    exampleMap.put("message", "Access denied to this resource");
    exampleMap.put("path", "/api/v1/admin/users");

    return new Example().value(exampleMap);
  }

  private Example createNotFoundExample() {
    Map<String, Object> exampleMap = new HashMap<>();
    exampleMap.put("timestamp", "2025-05-11T12:34:56.789");
    exampleMap.put("status", 404);
    exampleMap.put("error", "Not Found");
    exampleMap.put("message", "User with ID 123e4567-e89b-12d3-a456-426614174000 not found");
    exampleMap.put("path", "/api/v1/users/123e4567-e89b-12d3-a456-426614174000");

    return new Example().value(exampleMap);
  }

  private Example createConflictExample() {
    Map<String, Object> exampleMap = new HashMap<>();
    exampleMap.put("timestamp", "2025-05-11T12:34:56.789");
    exampleMap.put("status", 409);
    exampleMap.put("error", "Conflict");
    exampleMap.put("message", "Username or email already exists");
    exampleMap.put("path", "/api/v1/users/register");

    return new Example().value(exampleMap);
  }

  private Example createInternalServerErrorExample() {
    Map<String, Object> exampleMap = new HashMap<>();
    exampleMap.put("timestamp", "2025-05-11T12:34:56.789");
    exampleMap.put("status", 500);
    exampleMap.put("error", "Internal Server Error");
    exampleMap.put("message", "An unexpected error occurred");
    exampleMap.put("path", "/api/v1/users/profile");

    return new Example().value(exampleMap);
  }
}
