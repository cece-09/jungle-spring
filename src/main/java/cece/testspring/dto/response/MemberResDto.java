package cece.testspring.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MemberResDto {
    private Long id;
    public MemberResDto(Long id) {
        this.id = id;
    }
}
