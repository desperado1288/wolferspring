package com.wolferx.wolferspring.jdbi.mapper;

import com.wolferx.wolferspring.entity.Comment;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper implements ResultSetMapper<Comment> {

    public Comment map(int index, ResultSet r, StatementContext ctx) throws SQLException
    {
        final Comment comment = new Comment(
            r.getLong("post_id"),
            r.getLong("user_id"),
            r.getLong("post_id"),
            r.getLong("music_id"),
            r.getString("comment_body"),
            r.getInt("status"),
            r.getDate("time_created"),
            r.getDate("time_updated")
        );
        return comment;
    }
}
