package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Long userId, String title, String tag, String slug, String body);

    List<Post> findAll();

    List<Post> findAllMeta();

    Post findById(Long postId);

}
