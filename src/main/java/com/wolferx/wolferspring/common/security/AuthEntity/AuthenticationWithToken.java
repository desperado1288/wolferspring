package com.wolferx.wolferspring.common.security.AuthEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

public class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {

    public AuthenticationWithToken(Object principal, Object credential) {
        super(principal, credential);
    }

    public AuthenticationWithToken(Object principal, Object credential, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credential, authorities);
    }

    public void setToken(String token) {
        setDetails(token);
    }

    public String getToken() {
        return (String) getDetails();
    }
}
