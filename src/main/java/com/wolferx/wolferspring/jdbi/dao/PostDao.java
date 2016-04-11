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
        @Bind("user_id") final Long userId,
        @Bind("post_title") final String post_title,
        @Bind("post_body") final String post_body,
        @Bind("post_cover_url") final String post_cover_url,
        @Bind("type") final Integer type,
        @Bind("music_ids") final String music_ids,
        @Bind("slug") final String slug,
        @Bind("tag") final String tag,
        @Bind("status") final Integer status,
        @Bind("time_created") final Date timeCreated,
        @Bind("time_updated") final Date timeUpdated);

    @SqlUpdate(
        "UPDATE post SET post_title = :post_title, post_body = :post_body, post_cover_url = :post_cover_url, type = :type, music_ids = :music_ids," +
        "slug = :slug, tag = :tag, time_updated = :time_updated WHERE post_id = :post_id")
    public abstract void updatePost(
        @Bind("post_id") final Long post_id,
        @Bind("post_title") final String post_title,
        @Bind("post_body") final String post_body,
        @Bind("post_cover_url") final String post_cover_url,
        @Bind("type") final Integer type,
        @Bind("music_ids") final String music_ids,
        @Bind("slug") final String slug,
        @Bind("tag") final String tag,
        @Bind("time_updated") final Date timeUpdated);

    @SqlUpdate("UPDATE post SET status = 0 WHERE post_id = :post_id")
    public abstract void delete(@Bind("post_id") final Long post_id);

    @SqlQuery("SELECT * FROM post WHERE status = :status")
    public abstract List<Post> getAll(@Bind("status") final Integer status);

    @SqlQuery("SELECT * FROM post WHERE post_id = :post_id")
    public abstract Post getById(@Bind("post_id") final Long postId);

    @Transaction
    public Post create(final Long userId, final String title, final String body, final String postCoverUrl,
                       final Integer type, final String musicIds, final String slug, final String tag,
                       final Integer status, final Date timeCreated, final Date timeUpdated) {

        final Long genPostId = insertPost(userId, title, body, postCoverUrl, type, musicIds, slug, tag, status, timeCreated, timeUpdated);
        return getById(genPostId);
    }

    @Transaction
    public Post update(final Long post_id, final String title, final String body, final String postCoverUrl, final Integer type,
                       final String musicids, final String slug, final String tag, final Date timeUpdated) {

        updatePost(post_id, title, body, postCoverUrl, type, musicids, slug, tag, timeUpdated);
        return getById(post_id);
    }
}
