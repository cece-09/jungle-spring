package cece.spring.controller;

import cece.spring.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
