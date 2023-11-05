package cece.spring.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSignupRequest extends UserLoginRequest {
    private String role;
}
