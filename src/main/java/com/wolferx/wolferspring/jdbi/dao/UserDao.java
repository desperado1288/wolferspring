package com.wolferx.wolferspring.jdbi.dao;

import com.wolferx.wolferspring.entity.User;
import com.wolferx.wolferspring.jdbi.mapper.UserMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;

@RegisterMapper(UserMapper.class)
public abstract class UserDao {

    @SqlUpdate(
        "INSERT INTO user(email, username, password, verified, access_level, status, last_login, time_created, time_updated)" +
            "VALUES (:email, :username, :password, :verified, :access_level, :status, :time_created, :time_created, :time_created)")
    @GetGeneratedKeys
    public abstract Long createUser(
        @Bind("email") final String email,
        @Bind("username") final String username,
        @Bind("password") final String password,
        @Bind("verified") final Integer verified,
        @Bind("access_level") final Integer accessLevel,
        @Bind("status") final Integer status,
        @Bind("time_created") final Date timeCreated);

    @SqlQuery("SELECT * FROM user WHERE user_id = :user_id")
    public abstract User getUserById(@Bind("user_id") final Long userId);

    @SqlQuery("SELECT * FROM user WHERE email = :email")
    public abstract User getUserByEmail(@Bind("email") final String email);

}
