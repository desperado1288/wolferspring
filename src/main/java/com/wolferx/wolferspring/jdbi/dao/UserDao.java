package com.wolferx.wolferspring.jdbi.dao;

import com.wolferx.wolferspring.entity.User;
import com.wolferx.wolferspring.jdbi.mapper.UserMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(UserMapper.class)
public abstract class UserDao {

    @SqlQuery("SELECT * FROM user WHERE user_id = :user_id")
    public abstract User getUserById(@Bind("user_id") final Long userId);

    @SqlQuery("SELECT * FROM user WHERE email = :email")
    public abstract User getUserByEmail(@Bind("email") final String email);

}
