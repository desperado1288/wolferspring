package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.exception.StorageException;
import com.wolferx.wolferspring.entity.Comment;

import java.util.List;
public interface CommentService {

    Comment createComment(Long user_id, Long post_id, Long music_id, String comment_body) throws StorageException;

    List<Comment> findAll(boolean all);

    List<Comment> findByPostId(Long post_id);

    Comment findByCommentId(Long comment_id);

    Comment updateById(Long comment_id, Long music_id, String comment_body);

    void deleteById(Long comment_id) throws StorageException;


}
