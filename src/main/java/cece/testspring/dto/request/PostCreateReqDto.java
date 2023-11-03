package cece.testspring.dto.request;

import lombok.Getter;

@Getter
public class PostCreateReqDto {
    private String title;
    private String password;
    private String contents;
}
