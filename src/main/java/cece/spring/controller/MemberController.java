package cece.spring.controller;

import cece.spring.dto.request.UserLoginRequest;
import cece.spring.dto.request.UserSignupRequest;
import cece.spring.dto.response.BaseApiResponse;
import cece.spring.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        /* Return signup.html */
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        /* Return login.html */
        return new ModelAndView("login");
    }

    /**
     * New user signup
     * @param token admin auth token if role is "admin"
     * @param request username, password, role
     * @return ResponseEntity
     */
    @PostMapping("/api/signup")
    public ResponseEntity<BaseApiResponse> signup(
            @RequestHeader(name = "Authorization", required = false) String token,
            @RequestBody @Valid UserSignupRequest request) {

        return memberService.signup(token, request);
    }

    /**
     * User login
     * @param request username, password
     * @return ResponseEntity with JWT token.
     */
    @PostMapping("/api/login")
    public ResponseEntity<BaseApiResponse> login(
            @RequestBody @Valid UserLoginRequest request) {

        return memberService.login(request);
    }
}
