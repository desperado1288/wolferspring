package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.exception.StorageException;
import com.wolferx.wolferspring.entity.Comment;
import com.wolferx.wolferspring.jdbi.dao.CommentDao;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpI implements CommentService {

    @Autowired
    private DBI dbi;

    private final CommentDao commentDao;
    private static final Integer STATUS_ACTIVE = 1;
    private static final Integer STATUS_INACTIVE = 0;

    @Autowired
    public CommentServiceImpI(final CommentDao dao) { this.commentDao = dao; }

    @Override
    public Comment createComment(Long user_id, Long post_id, Long music_id, String comment_body)
        throws StorageException {
        final Integer status = STATUS_ACTIVE;
        final Date timeUpdated, timeCreated;
        timeCreated = timeUpdated = new Date();

        Comment comment;
        try {
            comment = this.commentDao.createComment(user_id, post_id, music_id, comment_body, status, timeUpdated, timeCreated);
        } catch (final DBIException error) {
            throw new StorageException(
                String.format("Error: [createComment] service for post: '%s' ", post_id), error);
        }
        return comment;
    }

    @Override
    public List<Comment> findAll(boolean all) {

        final List<Comment> clist;

        if(all) {
            clist = this.commentDao.findAll();
        } else {
            clist = this.commentDao.findAllValid();
        }
        return clist;
    }

    @Override
    public List<Comment> findByPostId(Long post_id) {

        final List<Comment> clist = this.commentDao.findAllValidByPostId(post_id);

        return clist;
    }

    @Override
    public Comment findByCommentId(Long comment_id) {

        final Comment c = this.commentDao.findByCommentId(comment_id);

        return c;
    }


    public Comment updateById(Long comment_id, Long music_id, String comment_body) {

        final Comment c = this.commentDao.updateComment(comment_id, music_id, comment_body);

        return c;
    }

    @Override
    public void deleteById(Long comment_id) throws StorageException {

        try {
            this.commentDao.delete(comment_id);
        } catch(final DBIException error) {
            throw new StorageException(String.format("Error: [deleteComment] service for comment: '%s' ", comment_id), error);
        }
    }
}
