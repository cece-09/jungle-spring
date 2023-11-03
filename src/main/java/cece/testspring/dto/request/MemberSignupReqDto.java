package cece.testspring.dto.request;

import cece.testspring.entity.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSignupReqDto extends MemberReqDto {
    private String role;
}
