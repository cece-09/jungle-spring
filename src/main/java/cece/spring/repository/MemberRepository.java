package cece.spring.repository;

import cece.spring.entity.Member;
import cece.spring.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, BaseRepository<Member, Long> {
    Optional<Member> findMemberByName(String name);
}
