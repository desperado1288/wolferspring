package com.wolferx.wolferspring.jdbi.dao;


import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.jdbi.mapper.PostCompleteMapper;
import com.wolferx.wolferspring.jdbi.mapper.PostDetailMapper;
import com.wolferx.wolferspring.jdbi.mapper.PostMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;
import java.util.List;

@RegisterMapper(PostCompleteMapper.class)
public abstract class PostDao {

    @SqlUpdate(
        "INSERT INTO post (user_id, title, tag, slug, status, time_created, time_updated) " +
        "VALUES (:user_id, :title, :tag, :slug, :status, :time_created, :time_updated)")
    @Mapper(PostMapper.class)
    @GetGeneratedKeys
    public abstract Long insertPost(
        @Bind("user_id") final Long userId,
        @Bind("title") final String title,
        @Bind("tag") final String tag,
        @Bind("slug") final String slug,
        @Bind("status") final Integer status,
        @Bind("time_created") final Date timeCreated,
        @Bind("time_updated") final Date timeUpdated);

    @SqlUpdate("INSERT INTO post_detail (post_id, post_body) VALUES (:post_id, :post_body)")
    @Mapper(PostDetailMapper.class)
    public abstract Integer insertPostDetail(
        @Bind("post_id") final Long postId,
        @Bind("post_body") final String postBody);

    @SqlQuery("SELECT P.*, PD.post_body FROM post as P LEFT JOIN post_detail as PD ON P.post_id = PD.post_id")
    public abstract List<Post> findAll();

    @SqlQuery("SELECT * FROM post")
    public abstract List<Post> findAllMeta();

    @SqlQuery("SELECT P.*, PD.post_body FROM post as P LEFT JOIN post_detail as PD ON P.post_id = PD.post_id WHERE P.post_id = :post_id")
    public  abstract Post findPostById(@Bind("post_id") final Long postId);

    @Transaction
    public Post createPost(Long userId, String title, String tag, String slug,
                           Integer status, String postBody, Date timeCreated, Date timeUpdated) {

        final Long genPostId = insertPost(userId, title, tag, slug, status, timeCreated, timeUpdated);
        insertPostDetail(genPostId, postBody);

        Post post = findPostById(genPostId);

        return post;
    }

    public abstract void close();
}
