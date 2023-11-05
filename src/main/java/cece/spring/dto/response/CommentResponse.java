package cece.spring.dto.response;

import cece.spring.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentResponse {
    private final Long id;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getMember().getName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
