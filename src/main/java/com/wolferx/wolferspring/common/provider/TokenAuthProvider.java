package com.wolferx.wolferspring.common.provider;

import com.google.common.base.Optional;
import com.wolferx.wolferspring.service.TokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class TokenAuthProvider implements AuthenticationProvider {

    private TokenService tokenService;

    public TokenAuthProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Optional<String> token = Optional.of(authentication.getPrincipal().toString());
        if (!token.isPresent() || token.get().isEmpty()) {
            throw new BadCredentialsException("Invalid token");
        }
        if (!tokenService.contains(token.get())) {
            throw new BadCredentialsException("Invalid token or token expired");
        }
        return tokenService.retrieve(token.get());
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
