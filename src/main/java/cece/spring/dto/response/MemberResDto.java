package cece.spring.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResDto {
    private Long id;
    public MemberResDto(Long id) {
        this.id = id;
    }
}
