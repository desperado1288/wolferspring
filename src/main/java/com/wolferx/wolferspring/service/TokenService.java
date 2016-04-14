package com.wolferx.wolferspring.service;


import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.util.Map;
import java.util.Optional;

public interface TokenService {

    String genToken(Long userId);

    String genRefreshToken(Long userId);

    Map<String, Object> verifyToken (String token) throws InternalAuthenticationServiceException;

    Optional<String> getRefreshTokenByUserId (final Long userId);
}
