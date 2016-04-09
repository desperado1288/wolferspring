package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.constant.Role;
import com.wolferx.wolferspring.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    AuthServiceImpl(final UserService userService, final TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public UsernamePasswordAuthenticationToken authWithPassword(final String email, final String password) {

        logger.info("<Start> authWithPassword() User: {}", email);
        // verify user existence
        final User user = userService.getUserByEmail(email)
            .orElseThrow(() -> {
                logger.error("<In> authWithPassword() Not get User: {}", email);
                return new AuthenticationServiceException("Unable to authenticate User with provided credentials");
            });

        // verify user password
        if (!password.equals(user.getPassword())) {
            logger.info("<In> authWithPassword() Invalid password for User: {}", email);
            throw new AuthenticationServiceException("Unable to authenticate User with provided credentials");
        }

        // grant user roles
        UsernamePasswordAuthenticationToken authentication;
        if (user.getAccessLevel().equals(Role.ADMIN.getValue())) {
            authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(Role.ADMIN.toString()));
        } else {
            authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(Role.USER.toString()));
        }

        // set token
        final String token = tokenService.signToken(user.getUserId());
        authentication.setDetails(token);

        logger.info("<End> authWithPassword() User: {}", email);
        return authentication;
    }

    @Override
    public PreAuthenticatedAuthenticationToken authWithToken(final String token) {

        logger.debug("<Start> authWithToken()");

        // verify token
        final Map<String, Object> payload = tokenService.verifyToken(token);

        // get user
        Long userId;
        try {
            userId  = ((Integer) payload.get("userId")).longValue();
        } catch (NullPointerException nullPointerException) {
            logger.error("<In> verifyToken() Not get userId from verified token. TokenPayload: {}", payload.toString());
            throw new InternalAuthenticationServiceException("Invalid Token!");
        }

        final User user = userService.getUserByUserId(userId)
            .orElseThrow(()-> {
                logger.error("<In> authWithToken() Not get user from verifyToken()");
                return new AuthenticationServiceException("Unable to authenticate User with provided token");
            });

        // set authentication
        PreAuthenticatedAuthenticationToken authentication;
        if (user.getAccessLevel().equals(Role.ADMIN.getValue())) {
            authentication = new PreAuthenticatedAuthenticationToken(user, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(Role.ADMIN.toString()));
        } else {
            authentication = new PreAuthenticatedAuthenticationToken(user, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(Role.USER.toString()));
        }

        authentication.setDetails(token);

        logger.debug("<End> authWithToken()");
        return authentication;
    }
}
