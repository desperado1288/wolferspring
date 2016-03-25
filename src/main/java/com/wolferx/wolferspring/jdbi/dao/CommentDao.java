package com.wolferx.wolferspring.jdbi.dao;

import com.wolferx.wolferspring.entity.Comment;
import com.wolferx.wolferspring.jdbi.mapper.CommentMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;
import java.util.List;

@RegisterMapper(CommentMapper.class)
public abstract class CommentDao {

    @SqlUpdate(
        "INSERT INTO comment(user_id, post_id, music_id, comment_body, status, time_created, time_updated)" +
            "VALUES (:user_id, :post_id, :music_id, :comment_body, :status, :time_created, :time_updated)")
    @GetGeneratedKeys
    public abstract Long insert(
        @Bind("user_id") final Long user_id,
        @Bind("post_id") final Long post_id,
        @Bind("music_id") final Long music_id,
        @Bind("comment_body") final String comment_body,
        @Bind("status") final Integer status,
        @Bind("time_created") final Date time_created,
        @Bind("time_updated") final Date time_updated
    );

    @SqlUpdate("UPDATE comment SET music_id = :music_id, comment_body = :comment_body WHERE comment_id = :comment_id")
    public abstract void update(
        @Bind("comment_id") final Long comment_id,
        @Bind("music_id") final Long music_id,
        @Bind("comment_body") final String comment_body
    );

    @SqlUpdate("UPDATE comment SET status = 0 WHERE comment_id = :comment_id")
    public abstract void delete(@Bind("comment_id") final Long comment_id);

    @SqlQuery("SELECT * FROM comment")
    public abstract List<Comment> findAll();

    @SqlQuery("SELECT * FROM comment WHERE comment_id = :comment_id")
    public abstract Comment findByCommentId(@Bind("comment_id") final Long comment_id);

    @SqlQuery("SELECT * FROM comment WHERE status = 1")
    public abstract List<Comment> findAllValid();

    @SqlQuery("SELECT * FROM comment WHERE post_id = :post_id")
    public abstract List<Comment> findAllByPostId(@Bind("post_id") final Long post_id);

    @SqlQuery("SELECT * FROM comment WHERE post_id := post_id AND status = 1")
    public abstract List<Comment> findAllValidByPostId(@Bind("post_id") final Long post_id);

    @Transaction
    public Comment createComment(Long user_id, Long post_id, Long music_id, String comment_body, Integer status, Date time_created, Date time_updated) {

        Long id = insert(user_id, post_id, music_id, comment_body, status, time_created, time_updated);

        Comment c = findByCommentId(id);

        return c;
    }

    @Transaction
    public Comment updateComment(Long comment_id, Long music_id, String comment_body) {

        update(comment_id, music_id, comment_body);

        Comment c = findByCommentId(comment_id);

        return c;
    }
    public abstract void close();

}
