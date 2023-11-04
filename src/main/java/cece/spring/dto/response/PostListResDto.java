package cece.spring.dto.response;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PostListResDto {
    private final int page;
    private final int size;
    private final long totalPage;
    private final long totalCount;
    private List<PostResDto> data = new ArrayList<>();
    public void addData(PostResDto postDto) {
        data.add(postDto);
    }
}
