package cece.spring.controller;


import cece.spring.dto.request.PostCreateRequest;
import cece.spring.dto.response.ApiResponse;
import cece.spring.service.PostService;
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
        return new ModelAndView("index");
    }

    @PostMapping("/api/posts")
    public ResponseEntity<ApiResponse> createPost(
            @RequestBody PostCreateRequest request,
            @RequestHeader(name = "Authorization") String token) {
        return postService.createPost(request, token);
    }

    @GetMapping("/api/posts")
    public ResponseEntity<ApiResponse> getPosts(
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
    public ResponseEntity<ApiResponse> getPost(
            @PathVariable Long id) {

        return postService.getPost(id);
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
    public ResponseEntity<ApiResponse> updatePost(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody PostCreateRequest request) {

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
    public ResponseEntity<ApiResponse> deletePost(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        return postService.deletePost(id, token);
    }
}
