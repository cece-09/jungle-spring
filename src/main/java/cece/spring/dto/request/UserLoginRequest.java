package cece.spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
    @NotBlank(message = "사용자 이름을 입력하세요")
    @Size(min = 4, max = 10, message = "사용자 이름은 4자 이상 10자 이하로 입력하세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}",
            message = "비밀번호는 8자 이상 15자 이하 대/소문자, 숫자, 특수문자를 포함해 입력하세요.")
    private String password;
}
