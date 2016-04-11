package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.entity.Comment;
import com.wolferx.wolferspring.jdbi.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpI implements CommentService {

    private final CommentDao commentDao;

    @Autowired
    public CommentServiceImpI(final CommentDao dao) { this.commentDao = dao; }

    @Override
    public Comment createComment(final Long userId, final Long postId, final Long musicId, final String commentBody) {

        final Date timeNow = new Date();
        return commentDao.create(userId, postId, musicId, commentBody, Constant.COMMENT_STATUS_ACTIVE, timeNow, timeNow);
    }

    @Override
    public List<Comment> getAllComment(final Integer status) {

        return commentDao.getAll(status);
    }

    @Override
    public Optional<List<Comment>> getCommentByPostId(final Long postId, final Integer status) {

        return Optional.ofNullable(commentDao.getAllByPostId(postId, status));
    }

    @Override
    public Optional<Comment> getCommentById(final Long commentId) {

        return Optional.ofNullable(commentDao.getById(commentId));
    }

    @Override
    public Comment updateCommentById(final Long commentId, final Long musicId, final String commentBody) {

        return commentDao.update(commentId, musicId, commentBody);
    }

    @Override
    public void deleteCommentById(final Long commentId) {

        commentDao.delete(commentId);
    }
}
