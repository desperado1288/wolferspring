package com.wolferx.wolferspring.controller;

import com.wolferx.wolferspring.common.exception.BaseServiceException;
import com.wolferx.wolferspring.config.RouteConfig;
import com.wolferx.wolferspring.entity.User;
import com.wolferx.wolferspring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping(value = RouteConfig.USER_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable("userId") final Long userId)
        throws BaseServiceException {

        logger.info("<Start> getUserByUserId(): UserId: {}", userId);
        final User user =  userService.getUserByUserId(userId);
        logger.info("<End> getUserByUserId(): UserId: {}", userId);

        return user;
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.GET)
    public User getUserByEmail(@PathVariable("email") final String email)
        throws BaseServiceException {

        logger.info("<Start> getUserByEmail(): Email: {}", email);
        final User user =  userService.getUserByEmail(email);
        logger.info("<End> getUserByEmail(): Email: {}", email);

        return user;
    }
}
