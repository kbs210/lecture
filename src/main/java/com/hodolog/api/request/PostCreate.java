package com.hodolog.api.request;


import com.hodolog.api.exception.InvalidRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@ToString
public class PostCreate {

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
    // Builder의 장점
    // 가독성이 좋음
    // 값 생성에 대한 유연성
    // 필요한 값만 받을 수 있다 -> 오버로딩 가능 조건 찾아보기
    // 객체의 불변성

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;
    @NotBlank(message = "콘텐츠을 입력해주세요")
    private String content;

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다");
        }
    }

}
