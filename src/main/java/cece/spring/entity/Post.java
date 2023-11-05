package cece.spring.entity;


import cece.spring.dto.request.PostCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Post(Long id, String title, String username, String contents) {
        this.title = title;
        this.content = contents;
    }

    public Post(PostCreateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void update(PostCreateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void setMember(Member member) {
        this.member = member;
        member.addPost(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }
}
