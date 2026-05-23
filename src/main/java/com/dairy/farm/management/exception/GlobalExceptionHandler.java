package com.dairy.farm.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*
 * Global exception handler for
 * centralized exception handling.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Handles resource not found exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>>
    handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        Map<String, Object> errorResponse =
                new HashMap<>();

        errorResponse.put(
                "timestamp",
                LocalDateTime.now());

        errorResponse.put(
                "status",
                HttpStatus.NOT_FOUND.value());

        errorResponse.put(
                "error",
                "Resource Not Found");

        errorResponse.put(
                "message",
                ex.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.NOT_FOUND);
    }

    /*
     * Handles generic exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    handleException(Exception ex) {

        Map<String, Object> errorResponse =
                new HashMap<>();

        errorResponse.put(
                "timestamp",
                LocalDateTime.now());

        errorResponse.put(
                "status",
                HttpStatus.INTERNAL_SERVER_ERROR.value());

        errorResponse.put(
                "error",
                "Internal Server Error");

        errorResponse.put(
                "message",
                ex.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}