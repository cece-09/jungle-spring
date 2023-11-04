package cece.spring.dto.response;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PostResDto {
    private final Long id;
    private final String title;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;
}