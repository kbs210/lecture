package com.hodolog.api.controller;

import com.hodolog.api.domain.Post;
import com.hodolog.api.exception.InvalidRequest;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.request.PostSearch;
import com.hodolog.api.response.PostResponse;
import com.hodolog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // SSR -> jsp, thymeleaf, mustache, freemarker
    //          -> html rendering
    // SPA ->
    //          vue   -> vue+SSR   = nuxt.js
    //                        -> javascript + < - > API(JSON)
    //          react -> react+SSR = next.js

    @GetMapping ("/posts")
    public String get() {
        return "Hello World";
    }

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT
    // 글 등록
    // POST Method

//    @PostMapping("/posts")
//    public String post(@RequestParam String title, @RequestParam String content) {
//        log.info("title={}, content={}", title, content);
//        return "Hello World";
//    }

//    @PostMapping("/posts")
//    public String post(@RequestParam Map<String, String> params) {
//        log.info("params={}", params);
//        String title = params.get("title");
//        return "Hello World";
//    }

//    @PostMapping("/posts")
//    public String post(PostCreate params) {
//        log.info("params={}", params.toString());
//        return "Hello World";
//    }

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params, BindingResult result) throws Exception {
        // 데이터를 검증하는 이유

        // 1. client 개발자가 깜빡할 수 있다. 실수로 값을 안보낼 수 있다.
        // 2. client bug로 값이 누락될 수 있다.
        // 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
        // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
        // 5. 서버 개발자의 편안함을 위해서

        log.info("params={}", params.toString());

// 스프링 부트 빈갑 체크 @NotBlank
//        String title = params.getTitle();
//        if (title == null || title.equals("")) {
//            // 1. 점검할 파라미터가 많을때 반복 작업 필요
//            // 2. 개발 팁 -> 무언가 3번 이상 반복작업을 할 때 내가 뭔가 잘못하고 있는건 아닐지 의심한다.
//            // 3. 누락 가능성
//            // 4. 생각보다 검증해야 할게 많다 ()
//                // {"title":""}
//                // {"title":"             "}
//                // {"title":"......수십억 글자....."}
//            // 5. 개발자스럽지 않다
//            throw new Exception("타이틀 값이 없어요");
//        }
//
//        String content = params.getContent();
//        if (content == null || content.equals("")) {
//            // error
//        }

        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField(); // title
            String errorMessage = firstFieldError.getDefaultMessage(); // 에러메세지

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;
        }

        return Map.of();
    }

    @PostMapping("/posts2")
    public void post2(@RequestBody @Valid PostCreate request) {
    //public Map<String, String> post2(@RequestBody @Valid PostCreate request) {
        // return 응답 형태 상황에 따라
        // 1. 저장한 데이터 Entity 를 다시 회신
        // 2. 저장한 데이터의 Primary_id 회신
        // 3. 응답 필요 없음
        // BAD CASE 서버에서 반드시 ~ 한다
        //          -> 서버에서 유연하게 대응이 더 좋음

//        if(request.getTitle().contains("바보")) {
//            throw new InvalidRequest();
//        }
        request.validate();

        postService.write(request);
        // return Map.of();
    }

    @GetMapping("/posts2/{postId}")
//    public Post get(@PathVariable(name = "postId") Long id) {
//        Post post = postService.get(id);
//        return post;
//    }
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        // Request 클래스
        // Response 클래스
        PostResponse response = postService.get10(id);
        return response;
    }

    @GetMapping("/posts2")
//     public List<PostResponse> getList(@RequestParam int page) {
//     public List<PostResponse> getList(@PageableDefault(size = 5) Pageable pageable) {
     public List<PostResponse> getList(Pageable pageable) {
        return postService.getList(pageable);
    }

    @GetMapping("/posts3")
//    public List<PostResponse> getList2(int page) {
    public List<PostResponse> getList2(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable(name = "postId") Long id, @RequestBody @Valid PostEdit request) {
        return postService.edit(id, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
