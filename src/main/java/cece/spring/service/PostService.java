package cece.spring.service;


import cece.spring.dto.request.PostCreateReqDto;
import cece.spring.dto.response.PostListResDto;
import cece.spring.dto.response.PostResDto;
import cece.spring.entity.Member;
import cece.spring.entity.Post;
import cece.spring.repository.MemberRepository;
import cece.spring.repository.PostRepository;
import cece.spring.response.ApiResponse;
import cece.spring.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@ComponentScan(basePackages = "cece.testspring.jwt")
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    /**
     * Create a new post with given token
     * @param postRequestDto title, content, password
     * @param token JWT token
     * @return save post in repository and return response
     */
    @Transactional
    public ResponseEntity<ApiResponse<Post>> createPost(PostCreateReqDto postRequestDto, String token) {
        // Validate member first.
        Long memberId = jwtUtils.getUserIdFromToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        // Create a new post
        Post post = new Post(postRequestDto);
        post.setMember(member);

        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(post, "ok", HttpStatus.CREATED.value())
        );
    }

    /**
     * Returns a list of post with pagination
     * @param page page number
     * @param size number of posts in one page
     * @return ResponseEntity of GetPostDto ApiResponse
     */
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PostListResDto>> getPosts(int page, int size) {
        // Get page data from request params.
        long totalCount = postRepository.count();
        long totalPage = totalCount / size;

        PostListResDto getPostsDto = new PostListResDto(page, size, totalPage, totalCount);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Post> postData = postRepository.findAll(pageable).getContent();

        // Set response data
        for (Post data : postData) {
            getPostsDto.addData(new PostResDto(
                    data.getId(),
                    data.getTitle(),
                    data.getMember().getName(),
                    data.getContents(),
                    data.getCreatedAt()
            ));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(getPostsDto, "ok", HttpStatus.CREATED.value())
        );
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
    }

    @Transactional
    public Long updatePost(Long id, PostCreateReqDto requestDto) throws AuthenticationException {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

//        if (!post.getPassword().equals(requestDto.getPassword())) {
//            // TODO: 예외처리 하고 싶음
//            return null;
//        }
        post.update(requestDto);
        return post.getId();
    }

    @Transactional
    public Long deletePost(Long id, String password) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
//        if (!post.getPassword().equals(requestDto.getPassword())) {
//            // TODO: 예외처리 하고 싶음
//            return null;
//        }
        postRepository.deleteById(id);
        return id;
    }

}
