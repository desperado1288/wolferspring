package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.exception.StorageServiceException;
import com.wolferx.wolferspring.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Long userId, String title, String body, String postCoverUrl, Integer type, String musicIds, String tag, String slug) throws StorageServiceException;

    List<Post> findAll(boolean all);


    Post findById(Long postId);

    Post updateById(Long postId, String title, String body, String postCoverUrl, Integer type, String musicIds, String slug, String tag) throws StorageServiceException;

    void deleteById(Long postId) throws StorageServiceException;

}
