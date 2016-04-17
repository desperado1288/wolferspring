package com.wolferx.wolferspring.entity;

import java.io.Serializable;
import java.sql.Date;

public class User implements Serializable {

    private Long userId;
    private String email;
    private String username;
    private String password;
    private Integer verified;
    private Integer accessLevel;
    private Integer status;
    private Date lastLogin;
    private Date timeCreated;
    private Date timeUpdated;

    protected User() { }

    public User(final Long userId, String email, String username, String password, Integer verified,
                Integer accessLevel, Integer status, Date lastLogin, Date timeCreated, Date timeUpdated) {

        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.verified = verified;
        this.accessLevel = accessLevel;
        this.status = status;
        this.lastLogin = lastLogin;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public Integer getAccessLevel() { return accessLevel; }

    public void setAccessLevel(Integer accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
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