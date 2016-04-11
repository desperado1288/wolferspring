package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.exception.BaseServiceException;
import com.wolferx.wolferspring.entity.User;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public interface AuthService {

    UsernamePasswordAuthenticationToken registerUser(String email, String password) throws BaseServiceException;

    UsernamePasswordAuthenticationToken authByPassword(String email, String password) throws AuthenticationServiceException;

    PreAuthenticatedAuthenticationToken authByToken(String token) throws AuthenticationServiceException;

    UsernamePasswordAuthenticationToken authByUser(User user);
}
