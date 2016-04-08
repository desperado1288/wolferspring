package com.wolferx.wolferspring.common.security.external;

import com.wolferx.wolferspring.common.security.AuthEntity.AuthenticationWithToken;

public interface ExternalServiceAuthenticator {

    AuthenticationWithToken authenticate(String username, String password);
}
