package cece.spring.entity;


import cece.spring.dto.request.PostRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "post")
@NoArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "post_id")
    @OrderBy("createdAt DESC")
    private final List<Comment> comments = new ArrayList<>();

    public Post(PostRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void update(PostRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void setMember(Member member) {
        /* ... */
        this.member = member;
    }

    public void addComment(Comment comment) {
        /* ... */
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        /* ... */
        this.comments.remove(comment);
    }
}


