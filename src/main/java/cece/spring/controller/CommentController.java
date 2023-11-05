package cece.spring.controller;

import cece.spring.dto.request.CommentRequest;
import cece.spring.dto.response.ApiResponse;
import cece.spring.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<ApiResponse> getComments(
            @PathVariable Long postId) {

        return commentService.getComments(postId);
    }

    @GetMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        return commentService.getComment(postId, commentId);
    }

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<ApiResponse> createComment(
            @PathVariable Long postId,
            @RequestHeader(value = "Authorization") String bearerToken,
            @RequestBody @Valid CommentRequest request) {
        return commentService.createComment(postId, request, bearerToken);
    }

    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader(value = "Authorization") String bearerToken,
            @RequestBody @Valid CommentRequest request) {

        return commentService.updateComment(postId, commentId, request, bearerToken);
    }

    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader(value = "Authorization") String bearerToken) {

        return commentService.deleteComment(postId, commentId, bearerToken);
    }
}
