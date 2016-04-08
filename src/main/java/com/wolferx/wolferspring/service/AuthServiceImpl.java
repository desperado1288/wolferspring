package com.wolferx.wolferspring.service;

import com.google.common.collect.ImmutableSet;
import com.wolferx.wolferspring.common.security.AuthEntity.AuthenticationWithToken;
import com.wolferx.wolferspring.entity.Role;
import com.wolferx.wolferspring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    AuthServiceImpl(final UserService userService, final TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public AuthenticationWithToken authWithPassword(final String email, final String password) {

        // validate user existence
        final User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new InternalAuthenticationServiceException("Unable to authenticate User with provided credentials"));

        // validate user password
        if (!password.equals(user.getPassword())) {
            throw new InternalAuthenticationServiceException("Unable to authenticate User with provided credentials");
        }

        AuthenticationWithToken authentication;

        // grant user roles
        if (user.getAccessLevel().equals(Role.ROLE_ADMIN)) {
            final SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
            authentication = new AuthenticationWithToken(email, password, ImmutableSet.of(authority));
        } else {
            final SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
            authentication = new AuthenticationWithToken(email, password, ImmutableSet.of(authority));
        }

        // generate token
        final String token = this.tokenService.generateNewToken();
        // store token
        tokenService.store(token, authentication);
        // set token
        authentication.setToken(token);

        return authentication;
    }
}
