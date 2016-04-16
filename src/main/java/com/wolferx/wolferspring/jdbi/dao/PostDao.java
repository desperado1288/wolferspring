package com.wolferx.wolferspring.jdbi.dao;


import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.jdbi.mapper.PostMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;
import java.util.List;

@RegisterMapper(PostMapper.class)
public abstract class PostDao {

    @SqlUpdate(
        "INSERT INTO post (user_id, post_title, post_body, post_cover_url, type, music_ids, slug, tag, status, time_created, time_updated) " +
        "VALUES (:user_id, :post_title, :post_body, :post_cover_url, :type, :music_ids, :slug, :tag, :status, :time_created, :time_updated)")
    @GetGeneratedKeys
    public abstract Long insertPost(
        @Bind("user_id") Long userId,
        @Bind("post_title") String post_title,
        @Bind("post_body") String post_body,
        @Bind("post_cover_url") String post_cover_url,
        @Bind("type") Integer type,
        @Bind("music_ids") String music_ids,
        @Bind("slug") String slug,
        @Bind("tag") String tag,
        @Bind("status") Integer status,
        @Bind("time_created") Date timeCreated,
        @Bind("time_updated") Date timeUpdated);

    @SqlUpdate(
        "UPDATE post SET post_title = :post_title, post_body = :post_body, post_cover_url = :post_cover_url, type = :type, music_ids = :music_ids, " +
        "slug = :slug, tag = :tag, time_updated = :time_updated WHERE post_id = :post_id")
    public abstract void updatePost(
        @Bind("post_id") Long post_id,
        @Bind("post_title") String post_title,
        @Bind("post_body") String post_body,
        @Bind("post_cover_url") String post_cover_url,
        @Bind("type") Integer type,
        @Bind("music_ids") String music_ids,
        @Bind("slug") String slug,
        @Bind("tag") String tag,
        @Bind("time_updated") Date timeUpdated);

    @SqlUpdate("UPDATE post SET status = 0 WHERE post_id = :post_id")
    public abstract void delete(@Bind("post_id") Long post_id);

    @SqlQuery("SELECT * FROM post WHERE status = :status")
    public abstract List<Post> getAll(@Bind("status") Integer status);

    @SqlQuery("SELECT * FROM post WHERE post_id = :post_id")
    public abstract Post getById(@Bind("post_id") Long postId);

    @Transaction
    public Post create(Long userId, String title, String body, String postCoverUrl,
                       Integer type, String musicIds, String slug, String tag,
                       Integer status, Date timeCreated, Date timeUpdated) {

        Long genPostId = insertPost(userId, title, body, postCoverUrl, type, musicIds, slug, tag, status, timeCreated, timeUpdated);
        return getById(genPostId);
    }

    @Transaction
    public Post update(Long postId, String title, String body, String postCoverUrl, Integer type,
                       String musicids, String slug, String tag, Date timeUpdated) {

        updatePost(postId, title, body, postCoverUrl, type, musicids, slug, tag, timeUpdated);
        return getById(postId);
    }

    abstract void close();
}
