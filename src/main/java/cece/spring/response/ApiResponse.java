package cece.spring.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final T data;
    private final String message;
    private final int statusCode;
}
