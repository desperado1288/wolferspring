package com.wolferx.wolferspring.common.security.provider;

import com.wolferx.wolferspring.service.AuthService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class PasswordAuthProvider implements AuthenticationProvider {

    private AuthService authService;

    public PasswordAuthProvider(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication)
        throws AuthenticationException {

        final String username = (String) authentication.getPrincipal();
        final String password = (String) authentication.getCredentials();

        return authService.authWithPassword(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
