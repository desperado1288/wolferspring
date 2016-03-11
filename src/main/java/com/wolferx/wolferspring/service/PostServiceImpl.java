package com.wolferx.wolferspring.service;

import com.google.common.collect.Lists;
import com.wolferx.wolferspring.domain.Post;
import com.wolferx.wolferspring.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post createPost(Long userId, String slug, String title, String tag) {

        Post post = new Post(userId, slug, title, tag);
        postRepository.save(post);

        return post;
    }

    @Override
    public List<Post> findAll() {

        return Lists.newArrayList(postRepository.findAll());
    }

    @Override
    public List<Post> findByUserId(Long userId) {

        return postRepository.findByUserId(userId);
    }

    @Override
    public Post findOne(Long postId) {

        return null;
    }

    @Override
    public void delete(Long postId) {

    }

    @Override
    public List<Post> findByName(String name) {
        return null;
    }

    @Override
    public List<Post> findByNameAndAuthor(String name, String author) {
        return null;
    }

    @Override
    public List<Post> findByPrice(Long price) {
        return null;
    }
}
