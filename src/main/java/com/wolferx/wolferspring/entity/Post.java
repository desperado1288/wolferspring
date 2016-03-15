package com.wolferx.wolferspring.entity;

import java.io.Serializable;
import java.sql.Date;

public class Post implements Serializable {

    private Long postId;
    private Long userId;
    private String title;
    private String tag;
    private String slug;
    private Integer status;
    private Date timeCreated;
    private Date timeUpdated;
    private String postBody;

    protected Post() { }

    public Post(Long postId, Long userId, String slug, String title, String tag, Integer status, Date timeCreated, Date timeUpdated, String postBody) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.tag = tag;
        this.slug = slug;
        this.status = status;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.postBody = postBody;
    }

    public Post(Long postId, Long userId, String slug, String title, String tag, Integer status, Date timeCreated, Date timeUpdated) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.tag = tag;
        this.slug = slug;
        this.status = status;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
    }

    public Post(Long userId, String slug, String title, String tag) {
        this.userId = userId;
        this.title = title;
        this.tag = tag;
        this.slug = slug;
        this.status = 1;
    }

    public Post(Long postId, String body) {
        this.postId = postId;
        this.postBody = body;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getPostBody() { return postBody; }

    public void setPostBody(String postBody) { this.postBody = postBody; }
}