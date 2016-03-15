package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.jdbi.dao.PostDao;
import org.skife.jdbi.v2.DBI;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Override
    public Post createPost(Long userId, String title, String tag,  String slug, String postBody) {

        final DBI dbi = new DBI("jdbc:mysql://localhost/wolferx_test", "wolferx", "wolferx");

        final PostDao postDao = dbi.open(PostDao.class);

        final Integer status = 1;
        final Date timeCreated = new Date();
        final Date timeUpdated = new Date();
        final Long genPostId = postDao.createPost(userId, title, tag, slug, status, timeCreated, timeUpdated);

        final Post post = postDao.findPostById(genPostId);

        final Integer genPostDetailResp = postDao.createPostDetail(genPostId, postBody);

        postDao.close();

        post.setPostBody(postBody);

        return post;
    }

    @Override
    public List<Post> findAll() {

        final DBI dbi = new DBI("jdbc:mysql://localhost/wolferx_test", "wolferx", "wolferx");

        final PostDao postDao = dbi.open(PostDao.class);

        final List<Post> posts = postDao.findAll();

        postDao.close();

        return posts;
    }

    @Override
    public Post findById(Long postId) {

        final DBI dbi = new DBI("jdbc:mysql://localhost/wolferx_test", "wolferx", "wolferx");

        final PostDao postDao = dbi.open(PostDao.class);

        final Post post = postDao.findPostById(postId);

        postDao.close();

        return post;
    }

    @Override
    public List<Post> findAllMeta() {

        final DBI dbi = new DBI("jdbc:mysql://localhost/wolferx_test", "wolferx", "wolferx");

        final PostDao postDao = dbi.open(PostDao.class);

        final List<Post> posts = postDao.findAllMeta();

        postDao.close();

        return posts;
    }

}
