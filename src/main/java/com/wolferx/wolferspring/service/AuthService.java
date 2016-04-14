package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.exception.BaseServiceException;
import com.wolferx.wolferspring.common.security.JWTAuthRefreshToken;
import com.wolferx.wolferspring.common.security.JWTAuthToken;
import com.wolferx.wolferspring.entity.User;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface AuthService {

    UsernamePasswordAuthenticationToken registerUser(String email, String password) throws BaseServiceException;

    UsernamePasswordAuthenticationToken authByPassword(String email, String password, Boolean rememberMe) throws AuthenticationServiceException;

    JWTAuthToken authByToken(String token) throws AuthenticationServiceException;

    JWTAuthRefreshToken authByRefreshToken(String refreshToken) throws AuthenticationServiceException;

    UsernamePasswordAuthenticationToken authByUser(User user);
}
