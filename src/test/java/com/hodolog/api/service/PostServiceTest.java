package com.hodolog.api.service;

import com.hodolog.api.domain.Post;
import com.hodolog.api.exception.PostNotFound;
import com.hodolog.api.repository.PostRepository;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.request.PostSearch;
import com.hodolog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다", post.getTitle());
        assertEquals("내용입니다", post.getContent());
        
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);
        Long postId = 1L;

        // when
        Post post = postService.get(requestPost.getId());

        // then
        assertNotNull(post);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test3() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);
        Long postId = 1L;

        // when
        PostResponse response = postService.get10(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test4() {
        // given
//        Post requestPost1 = Post.builder()
//                .title("foo")
//                .content("bar")
//                .build();
//        postRepository.save(requestPost1);
//
//        Post requestPost2 = Post.builder()
//                .title("foo2")
//                .content("bar2")
//                .build();
//        postRepository.save(requestPost2);
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post.builder()
                        .title("foo2")
                        .content("bar2")
                        .build()
        ));

        // when
        List<PostResponse> posts = postService.getList();

        // then
        assertEquals(2L, posts.size());

    }

    @Test
    @DisplayName("글 페이지 조회1")
    void test5() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                        .mapToObj(i -> {
                            return Post.builder()
                                    .title("호돌맨 제목 - " + i)
                                    .content("반포자이 - " + i)
                                    .build();
                        })
                        .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // when
        // List<PostResponse> posts = postService.getList(0);

        Pageable pageable = PageRequest.of(0, 5, DESC, "id");
        List<PostResponse> posts = postService.getList(pageable);

        // then
        assertEquals(5L, posts.size());
        assertEquals("호돌맨 제목 - 30", posts.get(0).getTitle());
        assertEquals("호돌맨 제목 - 26", posts.get(4).getTitle());

    }

    @Test
    @DisplayName("글 페이지 조회2")
    void test6() {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("foo - " + i)
                            .content("bar - " + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("foo - 19", posts.get(0).getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() {
        // given
        Post post = Post.builder()
                    .title("호돌맨")
                    .content("반포자이")
                    .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .build();
        // when
        postService.edit(post.getId(), postEdit);


        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다. id= " + post.getId()));

        assertEquals("호돌걸", changedPost.getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test8() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .build();
        // when
        postService.edit(post.getId(), postEdit);


        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다. id= " + post.getId()));

        assertEquals("호돌걸", changedPost.getTitle());
        assertEquals("반포자이", changedPost.getContent());

    }

    @Test
    @DisplayName("글 삭제")
    void test9() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());


        // then
        assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("글 1개 조회 실패")
    void test10() {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);
        Long postId = 1L;

        // expected
//        assertThrows(IllegalArgumentException.class, () -> {
//            PostResponse response = postService.get10(post.getId() + 1L);
//        }, "잘못된 예외처리입니다");

//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
//            postService.get(post.getId() + 1L);
//        });
//
//        Assertions.assertEquals("존재하지 않는 글입니다", e.getMessage());

        assertThrows(PostNotFound.class, () -> {
            postService.get10(post.getId() + 1L);
        });


    }
}