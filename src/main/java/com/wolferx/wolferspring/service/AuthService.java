package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.security.AuthEntity.AuthenticationWithToken;

public interface AuthService {

    AuthenticationWithToken authWithPassword(String email, String password);
}
