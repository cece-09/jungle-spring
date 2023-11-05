package cece.spring.utils;


import cece.spring.entity.Member;
import cece.spring.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthProvider {
    private static final String INVALID_TOKEN = "유효한 토큰이 아닙니다.";
    private static final String NO_USER_FOUND = "사용자를 찾을 수 없습니다.";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    /* from application.properties */
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final MemberRepository memberRepository;

    /**
     * Constructor executed
     * after created and dependency injected
     */
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Create JWT token from Member
     *
     * @param member user
     * @return token string
     */
    public String createToken(Member member) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(member.getId().toString())
//                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /**
     * Resolve JWT token
     *
     * @param bearerToken token
     * @return token string
     */
    private String resolveToken(String bearerToken) {
        if (!StringUtils.isEmpty(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.split(" ")[1].trim();
        }
        return null;
    }

    /**
     * Parse bearer token and validate token.
     *
     * @param token token with bearer prefix
     * @return Token if token is valid else null.
     */
    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims empty");
        }
        return false;
    }

    /**
     * Get member object from the token
     *
     * @param token token
     * @return Member object
     */
    private Member getUserFromToken(String token) {
        String subject = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody()
                .getSubject();

        Long id = Long.parseLong(subject);
        return memberRepository.findByIdOrThrow(id, NO_USER_FOUND);
    }

    /**
     * Authenticate and authorize bearer token.
     * @param bearerToken token with bearer prefix
     * @return Member object
     */
    public Member auth(String bearerToken) {
        String token = resolveToken(bearerToken);
        if (token == null || !validateToken(token)) {
            throw new JwtException(INVALID_TOKEN);
        }

        return getUserFromToken(token);
    }
}
