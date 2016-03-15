package com.wolferx.wolferspring.data.dao;


import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.data.mapper.PostDetailMapper;
import com.wolferx.wolferspring.data.mapper.PostMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;
import java.util.List;

@RegisterMapper(PostMapper.class)
public interface PostDao {

    @SqlUpdate(
        "INSERT INTO post (user_id, title, tag, slug, status, time_created, time_updated) " +
        "VALUES (:user_id, :title, :tag, :slug, :status, :time_created, :time_updated)")
    @GetGeneratedKeys
    Long createPost(
        @Bind("user_id") final Long userId,
        @Bind("title") final String title,
        @Bind("tag") final String tag,
        @Bind("slug") final String slug,
        @Bind("status") final Integer status,
        @Bind("time_created") final Date timeCreated,
        @Bind("time_updated") final Date timeUpdated);

    @SqlQuery("SELECT * FROM post LEFT JOIN post_detail ON post.post_id = post_detail.post_id")
    List<Post> findAll();

    @SqlQuery("SELECT * FROM post")
    List<Post> findAllMeta();

    @SqlQuery("SELECT * FROM post where post_id = :post_id")
    Post findPostById(@Bind("post_id") final Long postId);

    @RegisterMapper(PostDetailMapper.class)
    @SqlUpdate("INSERT INTO post_detail (post_id, post_body) VALUES (:post_id, :post_body)")
    Integer createPostDetail(
        @Bind("post_id") final Long postId,
        @Bind("post_body") final String postBody);

    @RegisterMapper(PostDetailMapper.class)
    @SqlQuery("SELECT * FROM post_detail where post_id = :post_id")
    Post findPostDetailById(@Bind("post_id") final Long postId);

    void close();
}
