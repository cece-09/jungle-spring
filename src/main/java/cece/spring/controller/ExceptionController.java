package cece.spring.controller;

import cece.spring.response.ApiResponse;
import cece.spring.utils.ResponseBuilder;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
        /* Handle ConstraintViolationException. */
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiResponse<Null>> handleConstraintViolationException(ConstraintViolationException e) {
            /* Parse error message. */
            String errorMessage = e.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .toList().toString();

            return ResponseBuilder.build(
                    HttpStatus.BAD_REQUEST,
                    errorMessage
            );
        }
}
