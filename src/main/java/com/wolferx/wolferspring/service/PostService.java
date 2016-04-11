package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<Post> getAllPost(Integer status);

    Optional<Post> getPostById(Long postId);

    Post createPost(Long userId, String title, String body, String postCoverUrl, Integer type, String musicIds, String tag, String slug);

    Post updatePostById(Long postId, String title, String body, String postCoverUrl, Integer type, String musicIds, String slug, String tag);

    void deletePostById(Long postId);

}
