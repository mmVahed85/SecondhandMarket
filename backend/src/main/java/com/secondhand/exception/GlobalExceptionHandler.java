package com.secondhand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.secondhand.util.ApiResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Object>> handleValidationErrors(
                MethodArgumentNotValidException e) {

        StringBuilder builder = new StringBuilder();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {

                        if (builder.length() > 0) {
                        builder.append("\n");
                        }

                        builder.append(error.getField())
                                .append(": ")
                                .append(error.getDefaultMessage());

                });

        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        false,
                        builder.toString(),
                        null
                )
        );
        }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException e) {

        return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponse<>(false, "Something went wrong", null)
        );
    }
}