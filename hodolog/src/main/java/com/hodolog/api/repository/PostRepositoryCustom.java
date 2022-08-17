package com.hodolog.api.repository;

import com.hodolog.api.domain.Post;
import com.hodolog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

//    List<Post> getList(int page);
    List<Post> getList(PostSearch postSearch);
}
