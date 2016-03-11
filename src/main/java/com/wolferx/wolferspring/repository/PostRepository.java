package com.wolferx.wolferspring.repository;

import com.wolferx.wolferspring.domain.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    public List<Post> findByUserId(Long userId);
}