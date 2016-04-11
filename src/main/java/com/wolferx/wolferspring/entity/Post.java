package com.wolferx.wolferspring.entity;

import java.io.Serializable;
import java.sql.Date;

public class Post implements Serializable {

    private Long postId;
    private Long userId;
    private String postTitle;
    private String postBody;
    private String postCoverUrl;
    private Integer type;
    private String musicIds;
    private String tag;
    private String slug;
    private Integer status;
    private Date timeCreated;
    private Date timeUpdated;

    protected Post() { }

    public Post(Long postId, Long userId, String postTitle, String postBody, String postCoverUrl, Integer type, String musicIds, String slug, String tag, Integer status, Date timeCreated, Date timeUpdated) {
        this.postId = postId;
        this.userId = userId;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postCoverUrl = postCoverUrl;
        this.type = type;
        this.musicIds = musicIds;
        this.tag = tag;
        this.slug = slug;
        this.status = status;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
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

    public String getPostTitle() { return postTitle; }

    public void setPostTitle(String title) { this.postTitle = title; }

    public String getPostBody() { return postBody; }

    public void setPostBody(String body) { this.postBody = body; }

    public String getPostCoverUrl() { return this.postCoverUrl; }

    public void setPostCoverUrl(String postCoverUrl) { this.postCoverUrl = postCoverUrl; }

    public String getMusicIds() { return this.musicIds; }

    public void setMusicIds(String musicIds) { this.musicIds = musicIds; }

    public Integer getType() { return this.type; }

    public void setType(Integer type) { this.type = type; }

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

}