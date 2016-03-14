package com.wolferx.wolferspring.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

@Entity
public class Post implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="post_id")
    private Long id;
    @Column(name="user_id", insertable = false, updatable = false)
    private Long userId;
    private String title;
    private String tag;
    private String slug;
    private Integer status;
    private Date time_created;
    private Date time_updated;

    // If response need join, then use this
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id")
    //private User user;

    protected Post() { }

    public Post(Long userId, String slug, String title, String tag) {
        this.userId = userId;
        this.title = title;
        this.tag = tag;
        this.slug = slug;
        this.status = 1;
    }

    @Column(name="post_id")
    public Long getId() {
        return id;
    }

    public void setId(Long postId) {
        this.id = postId;
    }

    @Column(name="user_id")
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

    public Date getTime_created() {
        return time_created;
    }

    public void setTime_created(Date time_created) {
        this.time_created = time_created;
    }

    public Date getTime_updated() {
        return time_updated;
    }

    public void setTime_updated(Date time_updated) {
        this.time_updated = time_updated;
    }

    /*
    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
    */
}