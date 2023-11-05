package cece.spring.dto.response;

import cece.spring.entity.Comment;
import cece.spring.entity.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@RequiredArgsConstructor
public class PostResponse {
    private final Long id;
    private final String title;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;
    private final List<CommentResponse> comments = new ArrayList<>();

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getMember().getName();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
    }

    public void addComment(Comment comment) {
        /* ... */
        comments.add(new CommentResponse(comment));
    }
}