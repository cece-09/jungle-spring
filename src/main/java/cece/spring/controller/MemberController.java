package cece.spring.controller;

import cece.spring.dto.request.MemberReqDto;
import cece.spring.dto.request.MemberSignupReqDto;
import cece.spring.dto.response.MemberResDto;
import cece.spring.response.ApiResponse;
import cece.spring.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

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
    public ResponseEntity<ApiResponse> signup(
            @RequestHeader(name = "Authorization", required = false) String token,
            @RequestBody @Valid MemberSignupReqDto request) {

        return memberService.signup(Optional.ofNullable(token), request);
    }

    /**
     * User login
     * @param request username, password
     * @return ResponseEntity with JWT token.
     */
    @PostMapping("/api/login")
    public ResponseEntity<ApiResponse> login(
            @RequestBody MemberReqDto request) {

        return memberService.login(request);
    }
}
