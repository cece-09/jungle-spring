package cece.spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSignupRequest extends UserLoginRequest {
    @NotBlank(message = "유저/관리자 정보를 입력하세요")
    @Pattern(regexp = "user|admin", message = "유저/관리자 정보를 입력하세요")
    private String role;
}
