package cece.spring.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
//@RequiredArgsConstructor
public class CommentRequest {
    @JsonCreator
    public CommentRequest(@JsonProperty("content") String content) {
        this.content = content;
    }

    @NotBlank(message = "내용을 입력하세요.")
    private final String content;
}
