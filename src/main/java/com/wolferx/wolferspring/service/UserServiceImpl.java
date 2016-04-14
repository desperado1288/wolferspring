package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.constant.Role;
import com.wolferx.wolferspring.entity.User;
import com.wolferx.wolferspring.jdbi.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(final String email, final String password) {

        final Date timeNow = new Date();
        final Long userId = userDao.create(email, null, password, Constant.USER_NOT_VERIFIED,
            Role.USER.getValue(), Constant.USER_STATUS_ACTIVE, timeNow);

        return userDao.getById(userId);
    }

    @Override
    public Optional<User> getUserByUserId(Long userId) {
        return Optional.ofNullable(userDao.getById(userId));
    }

    @Override
    public Optional<User> getUserByEmail(String email) { return Optional.ofNullable(userDao.getByEmail(email)); }

}
