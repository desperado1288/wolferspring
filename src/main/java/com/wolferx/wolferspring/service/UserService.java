package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.entity.User;

import java.util.Optional;


public interface UserService {

    User createUser(String email, String password);

    Optional<User> getUserByUserId(Long userId);

    Optional<User> getUserByEmail(String email);

}
