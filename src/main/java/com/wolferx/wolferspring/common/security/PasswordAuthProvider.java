package com.wolferx.wolferspring.common.security;

import com.wolferx.wolferspring.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class PasswordAuthProvider implements AuthenticationProvider {

    private AuthService authService;

    @Autowired
    public PasswordAuthProvider(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication)
        throws AuthenticationException {

        final String username = (String) authentication.getPrincipal();
        final String password = (String) authentication.getCredentials();

        return authService.authByPassword(username, password);
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
