package com.wolferx.wolferspring.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wolferx.wolferspring.entity.Token;

public class TokenResponse {

    @JsonProperty
    private String token;
    @JsonProperty
    private String refresh_token;

    public TokenResponse() { }

    public TokenResponse(final Token token) {
        this.token = token.getToken();
        if (token.hasRefreshToken()) {
            this.refresh_token = token.getRefreshToken();
        }
    }
}
