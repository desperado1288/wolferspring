package com.wolferx.wolferspring.jdbi.dao;

import com.wolferx.wolferspring.entity.Comment;
import com.wolferx.wolferspring.jdbi.mapper.CommentMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;
import java.util.List;

@RegisterMapper(CommentMapper.class)
public abstract class CommentDao {

    @SqlQuery("SELECT * FROM comment WHERE status = :status")
    public abstract List<Comment> getAll(@Bind("status") final Integer status);

    @SqlQuery("SELECT * FROM comment WHERE comment_id = :comment_id")
    public abstract Comment getById(@Bind("comment_id") final Long commentId);

    @SqlQuery("SELECT * FROM comment WHERE post_id := post_id AND status = :status")
    public abstract List<Comment> getAllByPostId(
        @Bind("post_id") final Long postId,
        @Bind("status") final Integer status);

    @SqlUpdate(
        "INSERT INTO comment(user_id, post_id, music_id, comment_body, status, time_created, time_updated)" +
            "VALUES (:user_id, :post_id, :music_id, :comment_body, :status, :time_created, :time_updated)")
    @GetGeneratedKeys
    public abstract Long create(
        @Bind("user_id") final Long userId,
        @Bind("post_id") final Long postId,
        @Bind("music_id") final Long musicId,
        @Bind("comment_body") final String commentBody,
        @Bind("status") final Integer status,
        @Bind("time_created") final Date timeCreated,
        @Bind("time_updated") final Date timeUpdated
    );

    @SqlUpdate("UPDATE comment SET music_id = :music_id, comment_body = :comment_body, time_updated = :time_updated WHERE comment_id = :comment_id")
    public abstract int update(
        @Bind("comment_id") final Long commentId,
        @Bind("music_id") final Long musicId,
        @Bind("comment_body") final String commentBody,
        @Bind("time_updated") final Date timeUpdated
    );

    @SqlUpdate("UPDATE comment SET status = 0 WHERE comment_id = :comment_id")
    public abstract int delete(@Bind("comment_id") final Long commentId);

    abstract void close();
}
