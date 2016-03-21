package com.wolferx.wolferspring.jdbi.mapper;

import com.wolferx.wolferspring.entity.Post;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostCompleteMapper implements ResultSetMapper<Post>
{
    public Post map(int index, ResultSet r, StatementContext ctx) throws SQLException
    {
        final Post post = new Post(
            r.getLong("post_id"),
            r.getLong("user_id"),
            r.getString("title"),
            r.getString("tag"),
            r.getString("slug"),
            r.getInt("status"),
            r.getDate("time_created"),
            r.getDate("time_updated"),
            r.getString("post_body")
        );

        return post;
    }
}
