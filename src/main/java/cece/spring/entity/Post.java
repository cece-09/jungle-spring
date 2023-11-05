package cece.spring.entity;


import cece.spring.dto.request.PostRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
