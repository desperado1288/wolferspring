package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.domain.Post;

import java.util.List;

public interface PostService {

    Post createPost(Long userId, String slug, String title, String tag);
    List<Post> findAll();
    List<Post> findByUserId(Long userId);
    Post findOne(Long postId);
    void delete(Long postId);
    List<Post> findByName(String name);
    List<Post> findByNameAndAuthor(String name, String author);
    List<Post> findByPrice(Long price);
}
