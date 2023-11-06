package cece.spring.controller;

import cece.spring.dto.request.CommentRequest;
import cece.spring.dto.response.BaseApiResponse;
import cece.spring.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // TODO: 정말 필요한지 생각해 볼 것
    @GetMapping("/api/comments/{commentId}")
    public ResponseEntity<BaseApiResponse> getComment(
            @PathVariable Long commentId) {

        return commentService.getComment(commentId);
    }

    @GetMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<BaseApiResponse> getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        return commentService.getComment(postId, commentId);
    }

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<BaseApiResponse> createComment(
            @PathVariable Long postId,
            @RequestHeader(value = "Authorization") String bearerToken,
            @RequestBody @Valid CommentRequest request) {

        return commentService.createComment(postId, request, bearerToken);
    }

    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<BaseApiResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader(value = "Authorization") String bearerToken,
            @RequestBody @Valid CommentRequest request) {

        return commentService.updateComment(postId, commentId, request, bearerToken);
    }

    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<BaseApiResponse> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader(value = "Authorization") String bearerToken) {

        return commentService.deleteComment(postId, commentId, bearerToken);
    }
}
