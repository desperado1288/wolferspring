package com.wolferx.wolferspring.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public interface AuthService {

    UsernamePasswordAuthenticationToken authWithPassword(String email, String password);

    PreAuthenticatedAuthenticationToken authWithToken(String token);
}
