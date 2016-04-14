package com.wolferx.wolferspring.common.security;

import com.wolferx.wolferspring.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JWTRefreshTokenAuthProvider implements AuthenticationProvider {

    private AuthService authService;

    @Autowired
    public JWTRefreshTokenAuthProvider(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication)
        throws AuthenticationException {

        final String refreshToken = (String) authentication.getPrincipal();
        return authService.authByRefreshToken(refreshToken);
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(JWTAuthRefreshToken.class);
    }
}
