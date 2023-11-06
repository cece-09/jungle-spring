package cece.spring.repository;

import cece.spring.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, BaseRepository<Post, Long> {
//    List<Post> findAllByOrderByModifiedAtDesc();
}
