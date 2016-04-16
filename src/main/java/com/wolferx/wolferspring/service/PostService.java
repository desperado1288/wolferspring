package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.jdbi.dao.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private final PostDao postDao;

    @Autowired
    PostService(final PostDao postDao) {
        this.postDao = postDao;
    }

    public List<Post> getAllPost(final Integer status) {
        return postDao.getAll(status);
    }

    public Optional<Post> getPostById(final Long postId) {
        return Optional.ofNullable(postDao.getById(postId));
    }

    public Post createPost(final Long userId, final String title, final String body, final String postCoverUrl,
                           final Integer type, final String musicIds, final String slug, final String tag) {

        final Date timeNow = new Date();
        return postDao.create(userId, title, body, postCoverUrl,
            type, musicIds, slug, tag, Constant.POST_STATUS_ACTIVE, timeNow, timeNow);
    }

    public Post updatePostById(final Long postId, final String title, final String body, final String postCoverUrl,
                               final Integer type, final String musicIds, final String slug, final String tag) {

        final Date timeUpdated = new Date();
        return postDao.update(postId, title, body, postCoverUrl, type, musicIds, slug, tag, timeUpdated);
    }

    public void deletePostById(final Long postId) {
        postDao.delete(postId);
    }

}
