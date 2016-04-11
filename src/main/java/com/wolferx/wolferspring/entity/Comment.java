package com.wolferx.wolferspring.entity;

import java.io.Serializable;
import java.sql.Date;

public class Comment implements Serializable{

    private Long commentId;
    private Long userId;
    private Long postId;
    private Long musicId;
    private String commentBody;
    private Integer status;
    private Date timeCreated;
    private Date timeUpdated;

    protected Comment(){}

    public Comment(Long commentId, Long userId, Long postId, Long musicId, String body, Integer status, Date timeCreated, Date timeUpdated) {
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.musicId = musicId;
        this.commentBody = body;
        this.status = status;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
    }

    public Long getCommentId() { return this.commentId; }

    public void setCommentId(Long id) { this.commentId = id; }

    public Long getUserId() { return this.userId; }

    public void setUserId(Long id) { this.userId = id; }

    public Long getPostId() { return this.postId; }

    public void setPostId(Long id) { this.postId = id; }

    public Long getMusicId() { return this.musicId; }

    public void setMusicId(Long id) { this.musicId = id; }

    public String getCommentBody() { return this.commentBody; }

    public void setCommentBody(String body) { this.commentBody = body; }

    public Integer getStatus() { return this.status; }

    public void setStatus(Integer s) { this.status = s; }

    public Date getTimeCreated() { return this.timeCreated; }

    public void setTimeCreated(Date time) { this.timeCreated = time; }

    public Date getTimeUpdated() { return this.timeUpdated; }

    public void setTimeUpdated(Date time) { this.timeUpdated = time; }

}
