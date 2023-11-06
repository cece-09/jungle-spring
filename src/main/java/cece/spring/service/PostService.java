package cece.spring.service;


import cece.spring.dto.request.PostRequest;
import cece.spring.dto.response.PostListResponse;
import cece.spring.dto.response.PostResponse;
import cece.spring.entity.Comment;
import cece.spring.entity.Member;
import cece.spring.entity.MemberRole;
import cece.spring.entity.Post;
import cece.spring.repository.CommentRepository;
import cece.spring.repository.PostRepository;
import cece.spring.dto.response.BaseApiResponse;
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

@Service
@RequiredArgsConstructor
public class PostService {
    private final AuthProvider authProvider;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /* Error message strings. */
    private final static String POST_NOT_FOUND = "존재하지 않는 게시글입니다.";
    private final static String PERMISSION_ERROR = "작성한 게시글만 수정/삭제할 수 있습니다.";
    private final static String POST_LIMIT_ERROR = "한 페이지당 최대 100개의 게시글을 노출합니다.";

    /* Max number of posts in one page. */
    private final static int GET_POSTS_LIMIT = 100;
    private final static int DEFAULT_PAGE = 1;
    private final static int DEFAULT_SIZE = 10;

    /**
     * Create a new post with given token
     *
     * @param request     title, content, password
     * @param bearerToken JWT token
     * @return save post in repository and return response
     */
    @Transactional
    public ResponseEntity<BaseApiResponse> createPost(
            PostRequest request, String bearerToken) {

        /* Authenticate member. */
        Member member = authProvider.auth(bearerToken);

        /* Create a new post */
        Post post = new Post(request);
        post.setMember(member);
        postRepository.save(post);

        /* Create a new post response */
        PostResponse response = new PostResponse(post);
        return BaseApiResponse.success(response);
    }

    /**
     * Returns a list of post with pagination
     *
     * @param page page number [start index is 1]
     * @param size number of posts in one page
     * @return ResponseEntity of GetPostDto ApiResponse
     */
    @Transactional(readOnly = true)
    public ResponseEntity<BaseApiResponse> getPosts(int page, int size) {
        /* If size exceeds limit, return error. */
        if (size > GET_POSTS_LIMIT) {
            throw new IllegalArgumentException(POST_LIMIT_ERROR);
        }

        /* Get page data from request params. */
        long totalCount = postRepository.count();
        long totalPage = ((totalCount - 1) / size) + 1;

        PostListResponse response = new PostListResponse(page, size, totalPage, totalCount);
        List<Post> postList = pagedPosts(page - 1, size);

        /* Set response data */
        for (Post post : postList) {
            PostResponse postResponse = new PostResponse(post);
            /* Get related comments. */
            List<Comment> commentList = pagedCommentsByPost(post.getId(), 0, 5);
            commentList.forEach(postResponse::addComment);

            /* Add to response data. */
            response.addData(postResponse);
        }

        /* Return response list */
        return BaseApiResponse.success(response);
    }

    /**
     * Get post by post id.
     *
     * @param id post id
     * @param page page number of comment
     * @param size page size of comment
     * @return ResponseEntity of post
     */
    @Transactional(readOnly = true)
    public ResponseEntity<BaseApiResponse> getPost(Long id, int page, int size) {
        Post post = postRepository.findByIdOrThrow(id, POST_NOT_FOUND);
        PostResponse response = new PostResponse(post);

        /* Get related comments. */
        List<Comment> commentList = pagedCommentsByPost(post.getId(), page-1, size);
        commentList.forEach(response::addComment);

        /* Return response. */
        return BaseApiResponse.success(response);
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
    public ResponseEntity<BaseApiResponse> updatePost(
            Long postId, PostRequest request, String bearerToken) {

        Post post = authPostAccess(postId, bearerToken);

        /* Update and return response. */
        post.update(request);
        PostResponse response = new PostResponse(post);
        return BaseApiResponse.success(response);
    }

    /**
     * Delete a certain post found by id.
     *
     * @param postId      post id
     * @param bearerToken jwt token
     * @return ResponseEntity of post id
     */
    @Transactional
    public ResponseEntity<BaseApiResponse> deletePost(Long postId, String bearerToken) {

        Post post = authPostAccess(postId, bearerToken);

        /* Delete and return response. */
        postRepository.deleteById(post.getId());

        /* Return response. */
        return BaseApiResponse.success(true);
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

        /* If user is neither admin nor author. */
        if (member.getRole() == MemberRole.USER && !member.getId().equals(author.getId())) {
            throw new AccessDeniedException(PERMISSION_ERROR);
        }

        /* If no error found. */
        return post;
    }

    /**
     * Returns a page of comments.
     *
     * @param postId post id
     * @param pageNumber page number
     * @param pageSize page size
     * @return List of comments in one page.
     */
    private List<Comment> pagedCommentsByPost(Long postId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return commentRepository.findByPostIdOrderByCreatedAt(postId, pageable).getContent();
    }

    /**
     * Returns a page of posts.
     *
     * @param pageNumber page number
     * @param pageSize page size
     * @return List of posts in one page.
     */
    private List<Post> pagedPosts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        return postRepository.findAll(pageable).getContent();
    }
}
