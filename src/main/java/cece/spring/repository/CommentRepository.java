package cece.spring.repository;

import cece.spring.entity.Comment;
import cece.spring.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long>, BaseRepository<Comment, Long> {
    @Query(value = "SELECT * FROM comment WHERE post_id = :postId ORDER BY created_at DESC",
            countQuery = "SELECT count(*) FROM Comment WHERE post_id = :postId",
            nativeQuery = true)
    Page<Comment> findByPostIdOrderByCreatedAt(Long postId, Pageable pageable);
}
