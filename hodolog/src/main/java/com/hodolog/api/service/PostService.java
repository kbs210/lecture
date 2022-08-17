package com.hodolog.api.service;

import com.hodolog.api.domain.Post;
import com.hodolog.api.domain.PostEditor;
import com.hodolog.api.exception.PostNotFound;
import com.hodolog.api.repository.PostRepository;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.request.PostSearch;
import com.hodolog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        // postCreate -> Entity
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);
    }

    public Post get(Long id) {
//         Optional<Post> postOptional = postRepository.findById(id);
//         if(postOptional.isPresent()) {
//             return postOptional.get();
//         }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));
        return post;
    }

    public PostResponse get10(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        // PostController -> WebPostService -> Repository
        //                   PostService

    }

    // 글이 많을 경우 -> 비용이 많이든다
    // DB가 뻗을 수 있다
    // DB -> 애플리케이션 서버 전달 시간, 트래픽 비용이 많이 든다
    public List<PostResponse> getList() {
//        return postRepository.findAll().stream()
//                .map(post -> PostResponse.builder()
//                        .id(post.getId())
//                        .title(post.getTitle())
//                        .content(post.getContent())
//                        .build())
//                .collect(Collectors.toList());
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getList(Pageable pageable) {
   // public List<PostResponse> getList(int page) {
        // Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));
        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

//    public List<PostResponse> getList2(int page) {
//        return postRepository.getList(page).stream()
//                .map(PostResponse::new)
//                .collect(Collectors.toList());
//    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                //.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));
                .orElseThrow(PostNotFound::new);

//        Entity에 setter 지양
//        post.setTitle(postEdit.getTitle());
//        post.setContent(postEdit.getContent());

//        post.change(postEdit.getTitle(), postEdit.getContent());

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        if(postEdit.getTitle() != null) {
            editorBuilder.title(postEdit.getTitle());
        }

        if(postEdit.getContent() != null) {
            editorBuilder.content(postEdit.getContent());
        }

        post.edit(editorBuilder.build());

        return new PostResponse(post);
//        PostEditor postEditor = editorBuilder
//                .title(postEdit.getTitle())
//                .content(postEdit.getContent())
//                .build();
//
//        post.edit(postEditor);

//        postRepository.save(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        postRepository.delete(post);
    }

}
