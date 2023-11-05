package cece.spring.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostCreateRequest {
    private String title;
    private String content;
}
