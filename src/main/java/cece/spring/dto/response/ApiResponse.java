package cece.spring.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Getter @Setter
@RequiredArgsConstructor
public class ApiResponse {
    private final Object body;
    private static final HttpHeaders DEFAULT_HEADERS;

    static {
        /* Set default headers. */
        DEFAULT_HEADERS = new HttpHeaders();
        DEFAULT_HEADERS.setContentType(MediaType.APPLICATION_JSON);
        // TODO: Add default headers.
    }

    /* Basic success method */
    public static ResponseEntity<ApiResponse> success(Object data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(DEFAULT_HEADERS)
                .body(new ApiResponse(data));
    }

    /* Success method with headers */
    public static ResponseEntity<ApiResponse> success(Object data, HttpHeaders headers) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(DEFAULT_HEADERS)
                .headers(headers)
                .body(new ApiResponse(data));
    }

    /* Success method with headers and status */
    public static ResponseEntity<ApiResponse> success(Object data, HttpHeaders headers, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .headers(DEFAULT_HEADERS)
                .headers(headers)
                .body(new ApiResponse(data));
    }

    /* Basic error method */
    public static ResponseEntity<ApiResponse> error(String message) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(DEFAULT_HEADERS)
                .body(new ApiResponse(message));
    }

    /* Error method with headers */
    public static ResponseEntity<ApiResponse> error(String message, HttpHeaders headers) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(DEFAULT_HEADERS)
                .headers(headers)
                .body(new ApiResponse(message));
    }


    /* Error method with headers and status */
    public static ResponseEntity<ApiResponse> error(String message, HttpHeaders headers, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .headers(DEFAULT_HEADERS)
                .headers(headers)
                .body(new ApiResponse(message));
    }
}
