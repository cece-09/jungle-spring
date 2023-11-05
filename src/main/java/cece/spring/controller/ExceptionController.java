package cece.spring.controller;

import cece.spring.dto.response.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ExceptionController {
    /* Handle ConstraintViolationException. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException e) {
        /* Parse error message. */
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList().toString();

        return ApiResponse.error(errorMessage);
    }

    /* Handle MethodArgumentNotValidException. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String errorMessage = Objects.requireNonNull(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        return ApiResponse.error(errorMessage);
    }

    /* Handle EntityNotFoundException. */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        String errorMessage = e.getMessage();
        return ApiResponse.error(errorMessage);
    }

    /* Handle AccessDeniedException. */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        String errorMessage = e.getMessage();
        return ApiResponse.error(errorMessage);
    }

    /* Handle JwtException. */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> handleJwtException(JwtException e) {
        String errorMessage = e.getMessage();
        return ApiResponse.error(errorMessage);
    }
}
