package cece.testspring.controller;


import cece.testspring.dto.request.PostCreateReqDto;
import cece.testspring.dto.response.PostListResDto;
import cece.testspring.entity.Post;
import cece.testspring.response.ApiResponse;
import cece.testspring.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Logger log = LoggerFactory.getLogger(getClass().getName());


    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @PostMapping("/api/posts")
    public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody PostCreateReqDto requestDto,
                           @RequestHeader(name = "Authorization") String token) {
        return postService.createPost(requestDto, token);
    }

    @GetMapping("/api/posts/list")
    public ResponseEntity<ApiResponse<PostListResDto>> getPosts(@RequestParam(value = "page", required = true) int page,
                                                                @RequestParam(value = "size", required = true) int size) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
        return postService.getPosts(page, size);
    }

    @GetMapping("/api/posts/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/api/posts/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostCreateReqDto requestDto) {
        return postService.updatePost(id, requestDto);
    }

//    @DeleteMapping("/api/posts/{id}")
//    public Long deletePost(@PathVariable Long id) {
//        return postService.deletePost(id);
//    }
}
