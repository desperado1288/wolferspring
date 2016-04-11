package com.wolferx.wolferspring.service;


import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.util.Map;

public interface TokenService {

    String signToken (Long userId );

    Map<String, Object> verifyToken (String token) throws InternalAuthenticationServiceException;

}
