package cece.spring.service;

import cece.spring.dto.request.CommentRequest;
import cece.spring.dto.response.BaseApiResponse;
import cece.spring.dto.response.CommentResponse;
import cece.spring.entity.Comment;
import cece.spring.entity.Member;
import cece.spring.entity.MemberRole;
import cece.spring.entity.Post;
import cece.spring.repository.CommentRepository;
import cece.spring.repository.PostRepository;
import cece.spring.utils.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthProvider authProvider;

    /* Error message strings. */
    private final static String POST_NOT_FOUND = "게시글을 찾을 수 없습니다.";
    private final static String COMMENT_NOT_FOUND = "댓글을 찾을 수 없습니다.";
    private final static String PERMISSION_ERROR = "작성한 댓글만 수정/삭제할 수 있습니다.";

    /**
     * Get a certain comment.
     *
     * @param postId    post id
     * @param commentId comment id
     * @return ResponseEntity of comment object
     */
    @Transactional(readOnly = true)
    public ResponseEntity<BaseApiResponse> getComment(
            Long postId, Long commentId) {

        /* Find post by postId to
         * validate proper access. */
        checkValidPost(postId);

        /* Find comment by commentId. */
        return getComment(commentId);
    }

    /**
     * Get a certain comment.
     *
     * @param commentId comment id
     * @return ResponseEntity of comment object
     */
    @Transactional(readOnly = true)
    public ResponseEntity<BaseApiResponse> getComment(Long commentId) {

        /* Find comment by commentId. */
        Comment comment = commentRepository.findByIdOrThrow(commentId, COMMENT_NOT_FOUND);
        CommentResponse response = new CommentResponse(comment);
        return BaseApiResponse.success(response);
    }

    /**
     * Create a comment.
     *
     * @param postId      post id
     * @param request     comment info
     * @param bearerToken JWT token
     * @return ResponseEntity of comment object
     */
    @Transactional
    public ResponseEntity<BaseApiResponse> createComment(
            Long postId, CommentRequest request, String bearerToken) {

        /* Validate token first.
         * Get member info from token. */
        Member member = authProvider.auth(bearerToken);

        /* Get post info from postId. */
        Post post = postRepository.findByIdOrThrow(postId, POST_NOT_FOUND);

        /* Create new comment and save db. */
        Comment comment = new Comment(request);
        post.addComment(comment);
        comment.setMember(member);
        commentRepository.save(comment);

        CommentResponse response = new CommentResponse(comment);
        return BaseApiResponse.success(response);
    }

    /**
     * Update a certain comment
     *
     * @param postId      post id
     * @param commentId   comment id
     * @param request     update info
     * @param bearerToken JWT token
     * @return ResponseEntity of comment id
     */
    @Transactional
    public ResponseEntity<BaseApiResponse> updateComment(
            Long postId, Long commentId, CommentRequest request, String bearerToken) {

        /* Get authorized comment access. */
        Comment authComment = authCommentAccess(postId, commentId, bearerToken);

        /* Update comment. */
        authComment.update(request);
        CommentResponse response = new CommentResponse(authComment);
        return BaseApiResponse.success(response);
    }

    /**
     * Delete a certain comment.
     *
     * @param postId      post id
     * @param commentId   comment id
     * @param bearerToken JWT token
     * @return ResponseEntity of comment id
     */
    @Transactional
    public ResponseEntity<BaseApiResponse> deleteComment(
            Long postId, Long commentId, String bearerToken) {

        /* Get authorized comment access. */
        Comment authComment = authCommentAccess(postId, commentId, bearerToken);

        /* Remove from post's comments list. */
        Post post = postRepository.findByIdOrThrow(postId, POST_NOT_FOUND);
        post.removeComment(authComment);

        /* Delete comment. */
        commentRepository.deleteById(authComment.getId());
        return BaseApiResponse.success(true);
    }

    /**
     * Handle authorization process
     * when user trying to access comment update or delete.
     *
     * @param postId      post id
     * @param commentId   comment id
     * @param bearerToken JWT token
     * @return Comment
     */
    private Comment authCommentAccess(Long postId, Long commentId, String bearerToken) {
        /* Validate token first.
         * Get member info from token. */
        Member member = authProvider.auth(bearerToken);

        /* Find post by postId to
         * validate proper access. */
        checkValidPost(postId);

        /* Get comment info from commentId. */
        Comment comment = commentRepository.findByIdOrThrow(commentId, COMMENT_NOT_FOUND);

        /* Authorize user. */
        Member author = comment.getMember();
        if (member.getRole() == MemberRole.USER && !member.getId().equals(author.getId())) {
            throw new AccessDeniedException(PERMISSION_ERROR);
        }

        return comment;
    }

    /**
     * Check if post exists in database.
     *
     * @param postId post id
     */
    private void checkValidPost(Long postId) {
        postRepository.findByIdOrThrow(postId, POST_NOT_FOUND);
        return;
    }
}
