package com.wolferx.wolferspring.jdbi.mapper;

import com.wolferx.wolferspring.entity.Post;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostMapper implements ResultSetMapper<Post>
{
    public Post map(int index, ResultSet r, StatementContext ctx) throws SQLException
    {
        final Post post = new Post(
            r.getLong("post_id"),
            r.getLong("user_id"),
            r.getString("post_title"),
            r.getString("post_body"),
            r.getString("post_cover_url"),
            r.getInt("type"),
            r.getString("music_ids"),
            r.getString("slug"),
            r.getString("tag"),
            r.getInt("status"),
            r.getDate("time_created"),
            r.getDate("time_updated")
        );

        return post;
    }
}