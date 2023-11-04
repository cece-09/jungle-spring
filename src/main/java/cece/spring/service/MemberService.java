package cece.spring.service;

import cece.spring.dto.request.MemberReqDto;
import cece.spring.dto.request.MemberSignupReqDto;
import cece.spring.dto.response.MemberResDto;
import cece.spring.entity.Member;
import cece.spring.entity.MemberRole;
import cece.spring.utils.JwtUtils;
import cece.spring.repository.MemberRepository;
import cece.spring.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final String ADMIN_TOKEN = "112233";

    /**
     * Create a member based on:
     *
     * @param request username, password
     * @return member info
     */
    @Transactional
    public ResponseEntity<ApiResponse> signup(
            Optional<String> isToken,
            MemberSignupReqDto request) {
        /* Extract user info and encrypt. */
        String username = request.getUsername();

        /* Validate password first. */
        String password = request.getPassword();
        if (!password.matches("(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}")) {
            return ApiResponse.error("비밀번호는 8자 이상 15자 이하 대소문자, 숫자, 특수문자를 포함해 입력");
        }

        String encodedPassword = encoder.encode(request.getPassword());
        String requestedRole = request.getRole();

        /* Validate admin token
         * if requested role is admin. */
        MemberRole role = MemberRole.USER;
        if (requestedRole.equals("admin")) {
            role = MemberRole.ADMIN;

            /* If token is empty. */
            if (isToken.isEmpty()) {
                return ApiResponse.error("관리자 인증 실패");
            }

            /* If token is not valid. */
            if (!isToken.get().equals(ADMIN_TOKEN)) {
                return ApiResponse.error("관리자 인증 실패");
            }
        }

        /* Handle non-unique username. */
        Optional<Member> found = memberRepository.findMemberByName(username);
        if (found.isPresent()) {
            return ApiResponse.error("이미 가입된 회원");
        }

        /* Save to database. */
        Member member = memberRepository.save(new Member(username, encodedPassword, role));

        /* Build Response and return. */
        return ApiResponse.success(new MemberResDto(member.getId()));
    }

    /**
     * User login
     *
     * @param request username, password
     * @return Response with JWT token
     */
    @Transactional
    public ResponseEntity<ApiResponse> login(MemberReqDto request) {
        /* Extract user info from request. */
        String username = request.getUsername();
        String password = request.getPassword();

        /* Find user by name in db. */
        Optional<Member> isMember = memberRepository.findMemberByName(username);
        if (isMember.isEmpty()) {
            return ApiResponse.error("존재하지 않는 유저");
        }

        /* Validate password. */
        Member member = isMember.get();
        if (!encoder.matches(password, member.getPassword())) {
            return ApiResponse.error("비밀번호 오류");
        }

        /* Create JWT token and return. */
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtUtils.createToken(member));
        return ApiResponse.success(new MemberResDto(member.getId()), headers);
    }

    @Transactional
    public Member logout() {
        return null;
    }
}
