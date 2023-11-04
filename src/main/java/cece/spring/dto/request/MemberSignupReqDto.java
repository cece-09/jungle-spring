package cece.spring.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSignupReqDto extends MemberReqDto {
    private String role;
}
