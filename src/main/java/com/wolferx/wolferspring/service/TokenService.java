package com.wolferx.wolferspring.service;


import java.util.Map;

public interface TokenService {

    String signToken (final Long userId );

    Map<String, Object> verifyToken (final String token);


}
