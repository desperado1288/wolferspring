package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.constant.Role;
import com.wolferx.wolferspring.common.exception.DuplicateItemException;
import com.wolferx.wolferspring.common.exception.ItemNotFoundException;
import com.wolferx.wolferspring.common.exception.StorageServiceException;
import com.wolferx.wolferspring.common.utils.CommonUtils;
import com.wolferx.wolferspring.entity.User;
import com.wolferx.wolferspring.jdbi.dao.UserDao;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByUserId(final Long userId)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final User user = userDao.getById(userId);
            if (user == null) {
                throw new ItemNotFoundException();
            }
            return user;

        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

    public User getUserByEmail(final String email)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final User user = userDao.getByEmail(email);
            if (user == null) {
                throw new ItemNotFoundException();
            }
            return user;

        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

    public User createUser(final String email, final String password, final String username)
        throws DuplicateItemException, ItemNotFoundException, StorageServiceException {

        try {
            final Date timeNow = new Date();
            final Long userId = userDao.create(email, username, password, Constant.USER_NOT_VERIFIED,
                Role.USER.getValue(), Constant.USER_STATUS_ACTIVE, timeNow, timeNow);
            return getUserByUserId(userId);

        } catch (final DBIException dbiException) {
            if (CommonUtils.isDuplicateEntryException(dbiException)) {
                throw new DuplicateItemException(dbiException);
            }
            throw new StorageServiceException(dbiException);
        }
    }

}
