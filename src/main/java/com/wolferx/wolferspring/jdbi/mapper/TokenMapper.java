package com.wolferx.wolferspring.jdbi.mapper;

import com.wolferx.wolferspring.entity.Token;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenMapper implements ResultSetMapper<Token> {

    public Token map(int index, ResultSet r, StatementContext ctx) throws SQLException
    {
        final Token token = new Token(
            r.getLong("user_id"),
            r.getString("device"),
            r.getString("ip"),
            r.getString("refresh_token"),
            r.getDate("time_created"),
            r.getDate("time_updated")
        );

        return token;
    }
}
