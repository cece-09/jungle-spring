package cece.spring.controller;

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
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    /**
     * New user signup
     * @param request username, password, role
     * @return ResponseEntity
     */
    @PostMapping("/api/signup")
    public ResponseEntity<ApiResponse<MemberResDto>> signup(
            @RequestHeader(name = "Authorization", required = false) String token,
            @RequestBody @Valid MemberSignupReqDto request) {

        return memberService.signup(Optional.ofNullable(token), request);
    }

    /**
     * Log in a user
     * @param signupDto username, password
     * @return ResponseEntity
     */
//    @PostMapping("/api/login")
//    public String login(@RequestBody MemberReqDto signupDto) {
//        log.debug("login request" + signupDto);
//        memberService.signup(signupDto);
//        return "redirect:/";
//    }
}
