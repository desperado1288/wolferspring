package com.wolferx.wolferspring.entity;

import java.io.Serializable;
import java.sql.Date;

public class Post implements Serializable {

    public static final Integer MUSIC_POST = 1;
    public static final Integer TEXT_POST = 2;

    private Long postId;
    private Long userId;
    private String post_title;
    private String post_body;
    private String post_cover_url;
    private Integer type;
    private String music_ids;
    private String tag;
    private String slug;
    private Integer status;
    private Date timeCreated;
    private Date timeUpdated;

    protected Post() { }
    //music post
    public Post(Long postId, Long userId, String post_title, String post_body, String post_cover_url, Integer type, String music_ids, String slug, String tag, Integer status, Date timeCreated, Date timeUpdated) {
        this.postId = postId;
        this.userId = userId;
        this.post_title = post_title;
        this.post_body = post_body;
        this.post_cover_url = post_cover_url;
        this.type = type;
        this.music_ids = music_ids;
        this.tag = tag;
        this.slug = slug;
        this.status = status;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
    }
    //text post
    public Post(Long postId, Long userId, String post_title, String post_body, Integer type, String slug, String tag, Integer status, Date timeCreated, Date timeUpdated) {
        this.postId = postId;
        this.userId = userId;
        this.post_title = post_title;
        this.post_body = post_body;
        this.tag = tag;
        this.type = type;
        this.slug = slug;
        this.status = status;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
    }

    public Post(Long userId, String post_title, String slug, String tag) {
        this.userId = userId;
        this.post_title = post_title;
        this.tag = tag;
        this.slug = slug;
        this.status = 1;
        this.type = 0;//TODO: type enum
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

    public String getPost_title() { return post_title; }

    public void setPost_title(String title) { this.post_title = title; }

    public String getPost_body() { return post_body; }

    public void setPost_body(String body) { this.post_body = body; }

    public String getPost_cover_url() { return this.post_cover_url; }

    public void setPost_cover_url(String post_cover_url) { this.post_cover_url = post_cover_url; }

    public String getMusic_ids() { return this.music_ids; }

    public void setMusic_ids(String music_ids) { this.music_ids = music_ids; }

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