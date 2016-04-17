package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.constant.Role;
import com.wolferx.wolferspring.common.exception.AuthServiceException;
import com.wolferx.wolferspring.common.exception.DuplicateItemException;
import com.wolferx.wolferspring.common.exception.ItemNotFoundException;
import com.wolferx.wolferspring.common.exception.StorageServiceException;
import com.wolferx.wolferspring.common.security.JWTAuthRefreshToken;
import com.wolferx.wolferspring.common.security.JWTAuthToken;
import com.wolferx.wolferspring.entity.Token;
import com.wolferx.wolferspring.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final static Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    AuthService(final UserService userService, final TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public UsernamePasswordAuthenticationToken registerUser(final String email, final String password, final String username)
        throws AuthServiceException {

        final String salt = BCrypt.gensalt(Constant.AUTH_JWT_SALT_LENGTH).concat(Constant.AUTH_JWT_SECRET);
        final String hash = BCrypt.hashpw(password, salt);
        final User user;
        try {
            user = userService.createUser(email, hash, username);
        } catch (final ItemNotFoundException | DuplicateItemException | StorageServiceException baseServiceException) {
            logger.error("<In> authByRefreshToken(): Caught BaseServiceException: ", baseServiceException.toString());
            throw new AuthServiceException();
        }
        return authByUser(user);
    }

    public UsernamePasswordAuthenticationToken authByPassword(final String email, final String password, final Boolean rememberMe)
        throws  AuthServiceException,
        StorageServiceException {

        logger.info("<Start> authWithPassword(): User: {}", email);
        // verify user existence
        final User user;
        try {
            user = userService.getUserByEmail(email);
        } catch (final ItemNotFoundException | StorageServiceException baseServiceException) {
            logger.error("<In> authByRefreshToken(): Caught BaseServiceException: ", baseServiceException.toString());
            throw new AuthServiceException();
        }

        // verify user password
        if (!BCrypt.checkpw(password, user.getPassword())) {
            logger.error("<In> authWithPassword(): Invalid password for User: {}", email);
            throw new AuthServiceException();
        }

        // grant user roles
        final UsernamePasswordAuthenticationToken authentication;
        if (user.getAccessLevel().equals(Role.ADMIN.getValue())) {
            authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(Role.ADMIN.toString()));
        } else {
            authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(Role.USER.toString()));
        }

        // set token
        final String token = tokenService.genToken(user.getUserId());
        final Token jwtToken = new Token(token);
        if (rememberMe) {
            final String refreshToken = tokenService.genRefreshToken(user.getUserId());
            jwtToken.setRefreshToken(refreshToken);
        }

        authentication.setDetails(jwtToken);

        logger.info("<End> authWithPassword(): User: {}", email);
        return authentication;
    }

    public JWTAuthToken authByToken(final String token)
        throws AuthServiceException {

        logger.debug("<Start> authWithToken()");
        // verify token
        final Map<String, Object> payload = tokenService.verifyToken(token);

        // get user
        final Long userId = ((Integer) payload.get("userId")).longValue();
        final User user;
        try {
            user = userService.getUserByUserId(userId);
        } catch (final ItemNotFoundException | StorageServiceException baseServiceException) {
            logger.error("<In> authByRefreshToken(): Caught BaseServiceException: ", baseServiceException.toString());
            throw new AuthServiceException();
        }

        // set authentication
        final JWTAuthToken authentication;
        if (user.getAccessLevel().equals(Role.ADMIN.getValue())) {
            authentication = new JWTAuthToken(user, null, AuthorityUtils.commaSeparatedStringToAuthorityList(Role.USER_AND_ADMIN.toString()));
        } else {
            authentication = new JWTAuthToken(user, null, AuthorityUtils.commaSeparatedStringToAuthorityList(Role.USER.toString()));
        }

        authentication.setDetails(token);

        logger.debug("<End> authWithToken()");
        return authentication;
    }

    public JWTAuthRefreshToken authByRefreshToken(final String refreshToken)
        throws AuthServiceException {

        logger.debug("<Start> authByRefreshToken()");
        // verify token
        final Map<String, Object> payload = tokenService.verifyToken(refreshToken);
        // get user
        final Long userId = ((Integer) payload.get("userId")).longValue();
        // get stored refresh token
        final String storedRefreshToken = tokenService.getRefreshTokenByUserId(userId);
        // validate refresh token
        if (!refreshToken.equals(storedRefreshToken)) {
            logger.error("<In> authByRefreshToken(): Expired stored RefreshToken : TokenPayload: {}", payload.toString());
            throw new AuthServiceException();
        }
        // generate new token
        final String newToken = tokenService.genToken(userId);
        final User user;
        try {
            user = userService.getUserByUserId(userId);
        } catch (final ItemNotFoundException | StorageServiceException baseServiceException) {
            logger.error("<In> authByRefreshToken(): Caught BaseServiceException: ", baseServiceException.toString());
            throw new AuthServiceException();
        }

        // set authentication
        final JWTAuthRefreshToken authentication;
        if (user.getAccessLevel().equals(Role.ADMIN.getValue())) {
            authentication = new JWTAuthRefreshToken(user, newToken, AuthorityUtils.commaSeparatedStringToAuthorityList(Role.USER_AND_ADMIN.toString()));
        } else {
            authentication = new JWTAuthRefreshToken(user, newToken, AuthorityUtils.commaSeparatedStringToAuthorityList(Role.USER.toString()));
        }

        authentication.setDetails(newToken);

        logger.debug("<End> authByRefreshToken()");
        return authentication;
    }

    public UsernamePasswordAuthenticationToken authByUser(final User user) {

        logger.debug("<Start> authWithUser()");
        // grant user roles
        final UsernamePasswordAuthenticationToken authentication;
        if (user.getAccessLevel().equals(Role.ADMIN.getValue())) {
            authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(Role.USER_AND_ADMIN.toString()));
        } else {
            authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList(Role.USER.toString()));
        }

        // set token
        final Token token = new Token(tokenService.genToken(user.getUserId()));

        authentication.setDetails(token);

        logger.debug("<End> authWithUser()");
        return authentication;
    }
}
