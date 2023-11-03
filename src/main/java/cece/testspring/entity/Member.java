package cece.testspring.entity;


import cece.testspring.dto.request.MemberReqDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity(name = "member")
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    @Size(min = 4, max = 10)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private MemberRole role;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();

    public Member(String name, String password, MemberRole role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void deletePost(Post post) {
        this.posts.remove(post);
    }
}

