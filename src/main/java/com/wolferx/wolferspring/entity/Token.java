package com.wolferx.wolferspring.entity;

import java.util.Date;

public class Token {

    private String token;
    private String refreshToken;
    private Long userId;
    private String device;
    private String ip;
    private Date timeCreated;
    private Date timeUpdated;

    public Token(String token) {
        this.token = token;
    }

    public Token(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public Token(Long userId, String device, String ip, String refreshToken, Date timeCreated, Date timeUpdated) {
        this.userId = userId;
        this.device = device;
        this.ip = ip;
        this.refreshToken = refreshToken;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Date timeUpdated) {
        this.timeUpdated = timeUpdated;
    }
}
