package cece.spring.entity;


import cece.spring.dto.request.CommentRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String content;

    public Comment(CommentRequest request) {
        this.content = request.getContent();
    }

    public void update(CommentRequest request) {
        /* ... */
        this.content = request.getContent();
    }
    public void setPost(Post post) {
        /* ... */
        this.post = post;
    }
    public void setMember(Member member) {
        /* ... */
        this.member = member;
    }
}
