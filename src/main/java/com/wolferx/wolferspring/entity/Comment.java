package com.wolferx.wolferspring.entity;

import java.io.Serializable;
import java.sql.Date;

public class Comment implements Serializable{
    private static final Integer STATUS_ACTIVE = 1;
    private static final Integer STATUS_INACTIVE = 0;


    private Long comment_id;
    private Long user_id;
    private Long post_id;
    private Long music_id;
    private String comment_body;
    private Integer status;
    private Date time_created;
    private Date time_updated;

    protected Comment(){}

    public Comment(Long commentId, Long userId, Long postId, Long musicId, String body, Integer s, Date timeCreated, Date timeUpdated) {
        this.comment_id = commentId;
        this.user_id = userId;
        this.post_id = postId;
        this.music_id = musicId;
        this.comment_body = body;
        this.status = s;
        this.time_created = timeCreated;
        this.time_updated = timeUpdated;
    }

    public Long getComment_id() { return this.comment_id; }

    public void setComment_id(Long id) { this.comment_id = id; }

    public Long getUser_id() { return this.user_id; }

    public void setUser_id(Long id) { this.user_id = id; }

    public Long getPost_id() { return this.post_id; }

    public void setPost_id(Long id) { this.post_id = id; }

    public Long getMusic_id() { return this.music_id; }

    public void setMusic_id(Long id) { this.music_id = id; }

    public String getComment_body() { return this.comment_body; }

    public void setComment_body(String body) { this.comment_body = body; }

    public Integer getStatus() { return this.status; }

    public void setStatus(Integer s) { this.status = s; }

    public Date getTime_created() { return this.time_created; }

    public void setTime_created(Date time) { this.time_created = time; }

    public Date getTime_updated() { return this.time_updated; }

    public void setTime_updated(Date time) { this.time_updated = time; }

}
