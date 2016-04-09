package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.entity.User;
import com.wolferx.wolferspring.jdbi.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    UserServiceImpl(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> getUserByUserId(Long userId) {
        return Optional.of(userDao.getUserById(userId));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userDao.getUserByEmail(email));
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }
}
