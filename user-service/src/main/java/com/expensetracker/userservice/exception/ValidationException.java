package com.expensetracker.userservice.exception;

import java.util.List;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
  private final List<String> errors;

  public ValidationException(String message, List<String> errors) {
    super(message);
    this.errors = errors;
  }
}
