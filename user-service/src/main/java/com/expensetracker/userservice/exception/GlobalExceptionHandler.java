package com.expensetracker.userservice.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.expensetracker.userservice.dto.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    String message = "Invalid request format";
    List<String> details = new ArrayList<>();

    // Extract meaningful information from the exception
    Throwable cause = ex.getCause();
    if (cause instanceof JsonMappingException) {
      // Handle unknown fields
      if (cause.getMessage().contains("Unrecognized field")) {
        // Extract field name from the error message
        String fieldName = extractFieldName(cause.getMessage());
        message = "Unknown field in request";
        details.add("The field '" + fieldName + "' is not expected in this request");
      } else {
        message = "Request format error";
        details.add("Please check the format of your request");
      }
    } else if (cause instanceof JsonParseException) {
      message = "Malformed JSON";
      details.add("Your request contains invalid JSON syntax");
    } else {
      details.add("The request could not be processed");
    }

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad Request")
            .message(message)
            .path(((ServletWebRequest) request).getRequest().getRequestURI())
            .details(details)
            .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // Helper method to extract field name from error message
  private String extractFieldName(String errorMessage) {
    // The field name is typically between quotes in the error message
    int startIndex = errorMessage.indexOf("\"");
    int endIndex = errorMessage.indexOf("\"", startIndex + 1);
    if (startIndex >= 0 && endIndex > startIndex) {
      return errorMessage.substring(startIndex + 1, endIndex);
    }
    return "unknown";
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(
      ResourceAlreadyExistsException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.CONFLICT, "Conflict", request.getRequestURI());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
      ResourceNotFoundException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Not Found", request.getRequestURI());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    // Get field errors
    List<String> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();

    // Get global errors
    List<String> globalErrors =
        ex.getBindingResult().getGlobalErrors().stream()
            .map(error -> error.getObjectName() + ": " + error.getDefaultMessage())
            .toList();

    // Combine all errors
    List<String> allErrors = new ArrayList<>();
    allErrors.addAll(fieldErrors);
    allErrors.addAll(globalErrors);

    // Add a specific message if an unknown field was provided
    if (ex.getMessage().contains("Unknown property")) {
      allErrors.add(
          "Request contains unknown fields that are not part of the expected request format");
    }

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Error")
            .message(
                "Input validation failed - please check the request format and field requirements")
            .path(((ServletWebRequest) request).getRequest().getRequestURI())
            .details(allErrors)
            .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // Generic exception handler for unexpected errors
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex, HttpServletRequest request) {
    return buildErrorResponse(
        ex, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", request.getRequestURI());
  }

  private ResponseEntity<ErrorResponse> buildErrorResponse(
      Exception ex, HttpStatus status, String error, String path) {

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status.value())
            .error(error)
            .message(ex.getMessage())
            .path(path)
            .build();

    return new ResponseEntity<>(errorResponse, status);
  }
}
