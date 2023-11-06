package cece.spring.repository;

import cece.spring.entity.Comment;
import cece.spring.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long>, BaseRepository<Comment, Long> {

//    List<Comment> findByPostOrderByCreatedAtDesc(Post post);

}
