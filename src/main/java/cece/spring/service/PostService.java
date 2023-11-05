package cece.spring.service;


import cece.spring.dto.request.PostCreateRequest;
import cece.spring.dto.response.PostListResponse;
import cece.spring.dto.response.PostResponse;
import cece.spring.entity.Member;
import cece.spring.entity.MemberRole;
import cece.spring.entity.Post;
import cece.spring.repository.MemberRepository;
import cece.spring.repository.PostRepository;
import cece.spring.dto.response.ApiResponse;
import cece.spring.utils.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthProvider authProvider;

    /* Error message strings. */
    private final static String POST_NOT_FOUND = "존재하지 않는 게시글입니다.";
    private final static String PERMISSION_ERROR = "작성한 게시글만 수정/삭제할 수 있습니다.";


    /**
     * Create a new post with given token
     *
     * @param request     title, content, password
     * @param bearerToken JWT token
     * @return save post in repository and return response
     */
    @Transactional
    public ResponseEntity<ApiResponse> createPost(
            PostCreateRequest request, String bearerToken) {

        /* Authenticate member. */
        Member member = authProvider.auth(bearerToken);

        /* Create a new post */
        Post post = new Post(request);
        post.setMember(member);
        postRepository.save(post);

        /* Create a new post response */
        PostResponse response = new PostResponse(post);
        return ApiResponse.success(response);
    }

    /**
     * Returns a list of post with pagination
     *
     * @param page page number [start index is 1]
     * @param size number of posts in one page
     * @return ResponseEntity of GetPostDto ApiResponse
     */
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getPosts(int page, int size) {
        /* Get page data from request params. */
        long totalCount = postRepository.count();
        long totalPage = ((totalCount - 1) / size) + 1;

        PostListResponse response = new PostListResponse(page, size, totalPage, totalCount);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        List<Post> postList = postRepository.findAll(pageable).getContent();

        /* Set response data */
        for (Post post : postList) {
            response.addData(new PostResponse(post));
        }
        /* Return response list */
        return ApiResponse.success(response);
    }

    /**
     * Get post by post id.
     *
     * @param id post id
     * @return ResponseEntity of post
     */
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getPost(Long id) {

        Post post = postRepository.findByIdOrThrow(id, POST_NOT_FOUND);
        PostResponse response = new PostResponse(post);
        return ApiResponse.success(response);
    }

    /**
     * Update post by given info.
     * Token authorization is necessary.
     *
     * @param postId      post id
     * @param request     update info
     * @param bearerToken user JWT token
     * @return ResponseEntity of post id
     * @throws AuthenticationException exp
     */
    @Transactional
    public ResponseEntity<ApiResponse> updatePost(
            Long postId, PostCreateRequest request, String bearerToken) {

        Post post = authPostAccess(postId, bearerToken);

        /* Update and return response. */
        post.update(request);
        return ApiResponse.success(post.getId());
    }

    /**
     * Delete a certain post found by id.
     *
     * @param postId      post id
     * @param bearerToken jwt token
     * @return ResponseEntity of post id
     */
    @Transactional
    public ResponseEntity<ApiResponse> deletePost(Long postId, String bearerToken) {

        Post post = authPostAccess(postId, bearerToken);

        /* Delete and return response. */
        postRepository.deleteById(post.getId());
        return ApiResponse.success(true);
    }


    /**
     * Handle error related to
     * authentication and authorization.
     *
     * @param postId      post id
     * @param bearerToken JWT token with bearer prefix
     * @return ResponseEntity if error, else null
     */
    private Post authPostAccess(Long postId, String bearerToken) {
        Member member = authProvider.auth(bearerToken);
        Post post = postRepository.findByIdOrThrow(postId, POST_NOT_FOUND);

        /* Authorization */
        Member author = post.getMember();

        if (member.getRole() == MemberRole.USER && !member.getId().equals(author.getId())) {
            throw new AccessDeniedException(PERMISSION_ERROR);
        }

        /* If no error found. */
        return post;
    }
}
