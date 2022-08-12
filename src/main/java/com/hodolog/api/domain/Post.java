package com.hodolog.api.domain;

import com.hodolog.api.request.PostEdit;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
//    서비스의 정책을 절대 이렇게 넣지 말것
//    public String getTitle(){
//        return this.title.substring(0, 10);
//    }

//    public void change(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
