package com.wolferx.wolferspring.common.provider;

import com.google.common.base.Optional;
import com.wolferx.wolferspring.external.AuthenticationWithToken;
import com.wolferx.wolferspring.external.ExternalServiceAuthenticator;
import com.wolferx.wolferspring.service.TokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class UserPasswordAuthProvider implements AuthenticationProvider {

    private TokenService tokenService;
    private ExternalServiceAuthenticator externalServiceAuthenticator;

    public UserPasswordAuthProvider(TokenService tokenService, ExternalServiceAuthenticator externalServiceAuthenticator) {
        this.tokenService = tokenService;
        this.externalServiceAuthenticator = externalServiceAuthenticator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> username = Optional.of(authentication.getPrincipal().toString());
        Optional<String> password = Optional.of(authentication.getCredentials().toString());

        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid User Credentials");
        }

        AuthenticationWithToken autuResult = externalServiceAuthenticator.authenticate(username.get(), password.get());
        String newToken = tokenService.generateNewToken();
        autuResult.setToken(newToken);
        tokenService.store(newToken, autuResult);

        return autuResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
