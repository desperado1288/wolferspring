package com.wolferx.wolferspring.service;

import com.google.common.base.Optional;
import com.wolferx.wolferspring.entity.User;

import java.util.Collection;


public interface UserService {

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();
}
