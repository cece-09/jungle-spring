package cece.spring.service;

import cece.spring.dto.request.UserLoginRequest;
import cece.spring.dto.request.UserSignupRequest;
import cece.spring.entity.Member;
import cece.spring.entity.MemberRole;
import cece.spring.utils.AuthProvider;
import cece.spring.repository.MemberRepository;
import cece.spring.dto.response.ApiResponse;
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
    private final AuthProvider authProvider;

    /* Admin authentication token. */
    private static final String ADMIN_TOKEN = "112233";
    private static final String ADMIN_AUTH_ERROR = "관리자 인증에 실패했습니다.";
    private static final String INVALID_USERNAME = "이미 가입된 회원입니다.";
    private static final String USER_NOT_FOUND = "가입된 회원 정보가 없습니다.";
    private static final String PASSWORD_ERROR = "비밀번호가 일치하지 않습니다.";
    private static final String SIGNUP_SUCCESS = "회원가입이 완료되었습니다.";
    private static final String LOGIN_SUCCESS = "로그인이 완료되었습니다.";


    /**
     * Create a member based on:
     *
     * @param request username, password
     * @return member info
     */
    @Transactional
    public ResponseEntity<ApiResponse> signup(
            Optional<String> isToken,
            UserSignupRequest request) {
        /* Extract user info and encrypt. */
        String username = request.getUsername();

        /* Encode password first. */
        String password = request.getPassword();
        String encodedPassword = encoder.encode(password);
        String requestedRole = request.getRole();

        /* Validate admin token
         * if requested role is admin. */
        MemberRole role = MemberRole.USER;
        if (requestedRole.equals("admin")) {
            role = MemberRole.ADMIN;

            /* If token is empty. */
            if (isToken.isEmpty()) {
                return ApiResponse.error(ADMIN_AUTH_ERROR);
            }

            /* If token is not valid. */
            if (!isToken.get().equals(ADMIN_TOKEN)) {
                return ApiResponse.error(ADMIN_AUTH_ERROR);
            }
        }

        /* Handle non-unique username. */
        Optional<Member> found = memberRepository.findMemberByName(username);
        if (found.isPresent()) {
            return ApiResponse.error(INVALID_USERNAME);
        }

        /* Save to database. */
        Member member = memberRepository.save(
                new Member(username, encodedPassword, role));

        /* Build Response and return. */
        return ApiResponse.success(SIGNUP_SUCCESS);
    }

    /**
     * User login
     *
     * @param request username, password
     * @return Response with JWT token
     */
    @Transactional
    public ResponseEntity<ApiResponse> login(UserLoginRequest request) {
        /* Extract user info from request. */
        String username = request.getUsername();
        String password = request.getPassword();

        /* Find user by name in db. */
        Optional<Member> isMember = memberRepository.findMemberByName(username);
        if (isMember.isEmpty()) {
            return ApiResponse.error(USER_NOT_FOUND);
        }

        /* Validate password. */
        Member member = isMember.get();
        if (!encoder.matches(password, member.getPassword())) {
            return ApiResponse.error(PASSWORD_ERROR);
        }

        /* Create JWT token and return. */
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authProvider.createToken(member));
        return ApiResponse.success(LOGIN_SUCCESS, headers);
    }
}
