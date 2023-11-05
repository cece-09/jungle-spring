package cece.spring.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@RequiredArgsConstructor
public class PostListResponse {
    private final int page;
    private final int size;
    private final long totalPage;
    private final long totalCount;
    private List<PostResponse> data = new ArrayList<>();
    public void addData(PostResponse post) {
        data.add(post);
    }
}
