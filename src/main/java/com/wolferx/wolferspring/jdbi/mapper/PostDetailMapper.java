package com.wolferx.wolferspring.jdbi.mapper;

import com.wolferx.wolferspring.entity.Post;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostDetailMapper implements ResultSetMapper<Post>
{
    public Post map(int index, ResultSet r, StatementContext ctx) throws SQLException
    {
        final Post post = new Post(
            r.getLong("post_id"),
            r.getString("post_body")
        );

        return post;
    }
}