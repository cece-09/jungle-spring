package cece.spring.utils;

import lombok.NoArgsConstructor;
import cece.spring.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@NoArgsConstructor
public final class ResponseBuilder {
    private static final HttpHeaders DEFAULT_HEADERS;

    static {
        /* Set default headers. */
        DEFAULT_HEADERS = new HttpHeaders();
        DEFAULT_HEADERS.setContentType(MediaType.APPLICATION_JSON);
        // TODO: Add default headers.
    }

    /* Build full response with status, message, headers and body. */
    public static <T> ResponseEntity<ApiResponse<T>> build(
            HttpStatus status,
            String message,
            HttpHeaders headers,
            T body) {

        return ResponseEntity
                .status(status)
                .headers(DEFAULT_HEADERS)
                .headers(headers)
                .body(
                        new ApiResponse<>(body, message, status.value())
                );
    }

    /* Build response with body, use default headers. */
    public static <T> ResponseEntity<ApiResponse<T>> build(
            HttpStatus status,
            String message,
            T body) {

        return ResponseEntity
                .status(status)
                .headers(DEFAULT_HEADERS)
                .body(
                        new ApiResponse<>(body, message, status.value())
                );
    }

    public static <T> ResponseEntity<ApiResponse<T>> build(
            HttpStatus status,
            String message) {

        return ResponseEntity
                .status(status)
                .headers(DEFAULT_HEADERS)
                .body(
                        new ApiResponse<>(null, message, status.value())
                );
    }
}
