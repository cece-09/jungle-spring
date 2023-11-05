package cece.spring.entity;


import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Member(String name, String password, MemberRole role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }
}

