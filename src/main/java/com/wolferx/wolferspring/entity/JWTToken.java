package com.wolferx.wolferspring.entity;

public class JWTToken {

    private String token;
    private String refreshToken;

    public JWTToken(final String token) {
        this.token = token;
    }

    public JWTToken(final String token, final String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public Boolean hasRefreshToken() {
        return refreshToken != null && !refreshToken.equals("");
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
