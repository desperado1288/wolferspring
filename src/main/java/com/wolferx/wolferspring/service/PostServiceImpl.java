package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.exception.StorageException;
import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.jdbi.dao.PostDao;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private DBI dbi;

    private final PostDao postDao;

    @Autowired
    PostServiceImpl(final PostDao postDao) {
        this.postDao = postDao;
    }

    @Override
    public Post createPost(Long userId, String title, String body, String postCoverUrl, Integer type, String musicids, String slug, String tag)
            throws StorageException {

        final Integer status = 1;
        final Date timeCreated, timeUpdated;
        timeCreated = timeUpdated = new Date();

        Post post;
        try {
            post = this.postDao.createPost(userId, title, body, postCoverUrl, type, musicids, slug, tag, status, timeCreated, timeUpdated);

        } catch (final DBIException error) {
            throw new StorageException(
                    String.format("Error: [createPost] service for user: '%s' ", userId), error);
        }

        return post;
    }

    @Override
    public List<Post> findAll() {

        final List<Post> posts = this.postDao.findAll();

        return posts;
    }

    @Override
    public Post findById(Long postId) {

        final Post post = this.postDao.findPostById(postId);

        return post;
    }

    @Override
    public Post updateById(Long postId, String title, String body, String postCoverUrl, Integer type, String musicIds, String slug, String tag, Integer status)
            throws StorageException{
        final Date timeUpdated = new Date();
        Post post;
        try {
            post = this.postDao.update(postId, title, body, postCoverUrl, type, musicIds, slug, tag, status, timeUpdated);
        } catch (final DBIException error) {
            throw new StorageException(String.format("Error: [updatePost] service for post: '%s' ", postId), error);
        }

        return post;
    }
}
