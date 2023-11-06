package cece.spring.controller;


import cece.spring.dto.request.PostRequest;
import cece.spring.dto.response.BaseApiResponse;
import cece.spring.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/")
    public ModelAndView home() {
        /* ... */
        return new ModelAndView("index");
    }

    @GetMapping("/api/posts")
    public ResponseEntity<BaseApiResponse> getPosts(
            @RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "size", required = true) int size) {

        return postService.getPosts(page, size);
    }

    /**
     * Get a certain post by id.
     *
     * @param id post id
     * @return ResponseEntity of post
     */
    @GetMapping("/api/posts/{id}")
    public ResponseEntity<BaseApiResponse> getPost(
            @PathVariable Long id) {

        return postService.getPost(id);
    }

    @PostMapping("/api/posts")
    public ResponseEntity<BaseApiResponse> createPost(
            @RequestBody @Valid PostRequest request,
            @RequestHeader(name = "Authorization") String token) {
        return postService.createPost(request, token);
    }

    /**
     * Update a certain post found by id.
     *
     * @param id      post id
     * @param token   JWT token
     * @param request update info
     * @return ResponseEntity of updated post id
     */
    @PutMapping("/api/posts/{id}")
    public ResponseEntity<BaseApiResponse> updatePost(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Valid PostRequest request) {

        return postService.updatePost(id, request, token);
    }

    /**
     * Delete a certain post found by id.
     *
     * @param id    post id
     * @param token JWT token
     * @return ResponseEntity of deleted post id
     */
    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<BaseApiResponse> deletePost(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        return postService.deletePost(id, token);
    }
}
