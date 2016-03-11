package com.wolferx.wolferspring.repository;

import com.wolferx.wolferspring.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findByUserId(Long userId);

}
