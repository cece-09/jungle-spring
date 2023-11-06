package cece.spring.controller;

import cece.spring.dto.response.BaseApiResponse;
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
    public ResponseEntity<BaseApiResponse> handleConstraintViolationException(ConstraintViolationException e) {
        /* Parse error message. */
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList().toString();

        return BaseApiResponse.error(errorMessage);
    }

    /* Handle MethodArgumentNotValidException. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String errorMessage = Objects.requireNonNull(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        return BaseApiResponse.error(errorMessage);
    }

    /* Handle EntityNotFoundException. */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseApiResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        String errorMessage = e.getMessage();
        return BaseApiResponse.error(errorMessage);
    }

    /* Handle AccessDeniedException. */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        String errorMessage = e.getMessage();
        return BaseApiResponse.error(errorMessage);
    }

    /* Handle JwtException. */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<BaseApiResponse> handleJwtException(JwtException e) {
        String errorMessage = e.getMessage();
        return BaseApiResponse.error(errorMessage);
    }

    /* Handle IllegalArgumentException. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        String errorMessage = e.getMessage();
        return BaseApiResponse.error(errorMessage);
    }
}
