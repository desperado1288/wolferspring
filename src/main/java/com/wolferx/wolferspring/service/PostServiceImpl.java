package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.data.dao.PostDao;
import org.skife.jdbi.v2.DBI;
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
    public Post createPost(Long userId, String title, String tag,  String slug, String postBody) {

        final Integer status = 1;
        final Date timeCreated, timeUpdated;
        timeCreated = timeUpdated = new Date();
        final Long genPostId = this.postDao.createPost(userId, title, tag, slug, status, timeCreated, timeUpdated);

        final Post post = this.postDao.findPostById(genPostId);

        final Integer genPostDetailResp = this.postDao.createPostDetail(genPostId, postBody);

        postDao.close();

        post.setPostBody(postBody);

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
    public List<Post> findAllMeta() {

        final List<Post> posts = this.postDao.findAllMeta();

        return posts;
    }

}
