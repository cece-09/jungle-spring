package cece.testspring.entity;


import cece.testspring.dto.request.PostCreateReqDto;
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
    private String password;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Post(Long id, String title, String username, String password, String contents) {
        this.title = title;
        this.password = password;
        this.contents = contents;
    }

    public Post(PostCreateReqDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
        this.password = postRequestDto.getPassword();
    }

    public void update(PostCreateReqDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
        this.password = postRequestDto.getPassword();
    }

    public void setMember(Member member) {
        this.member = member;
        member.addPost(this);
    }
}
