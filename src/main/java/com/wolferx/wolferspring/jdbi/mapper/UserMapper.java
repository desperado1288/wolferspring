package com.wolferx.wolferspring.jdbi.mapper;

import com.wolferx.wolferspring.entity.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User> {

    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException
    {
        final User user = new User(
            r.getLong("user_id"),
            r.getString("email"),
            r.getString("username"),
            r.getString("password"),
            r.getInt("verified"),
            r.getInt("access_level"),
            r.getInt("status"),
            r.getDate("last_login"),
            r.getDate("time_created"),
            r.getDate("time_updated")
        );

        return user;
    }
}
