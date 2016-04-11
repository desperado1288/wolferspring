package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> getAllComment(Integer status);

    Optional<Comment> getCommentById(Long commentId);

    Optional<List<Comment>> getCommentByPostId(Long postId, Integer status);

    Comment createComment(Long userId, Long postId, Long musicId, String commentBody);

    Comment updateCommentById(Long commentId, Long musicId, String commentBody);

    void deleteCommentById(Long commentId);

}
