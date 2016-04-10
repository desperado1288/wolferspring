package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.exception.BaseServiceException;
import com.wolferx.wolferspring.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public interface AuthService {

    UsernamePasswordAuthenticationToken authWithPassword(String email, String password);

    PreAuthenticatedAuthenticationToken authWithToken(String token);

    UsernamePasswordAuthenticationToken authWithUser(User user);

    UsernamePasswordAuthenticationToken registerUser(String email, String password) throws BaseServiceException;
}
