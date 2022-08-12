package com.hodolog.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostEdit {

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;
    @NotBlank(message = "콘텐츠을 입력해주세요")
    private String content;
}
