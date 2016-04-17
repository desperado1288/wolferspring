package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.exception.DuplicateItemException;
import com.wolferx.wolferspring.common.exception.ItemNotFoundException;
import com.wolferx.wolferspring.common.exception.StorageServiceException;
import com.wolferx.wolferspring.common.utils.CommonUtils;
import com.wolferx.wolferspring.entity.Comment;
import com.wolferx.wolferspring.jdbi.dao.CommentDao;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentDao commentDao;

    @Autowired
    public CommentService(final CommentDao dao) { this.commentDao = dao; }

    public List<Comment> getAllComment(final Integer status)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final List<Comment> commentList = commentDao.getAll(status);
            if (commentList.isEmpty()) {
                throw new ItemNotFoundException();
            }
            return commentList;

        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

    public List<Comment> getCommentByPostId(final Long postId, final Integer status)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final List<Comment> commentList = commentDao.getAllByPostId(postId, status);
            if (commentList.isEmpty()) {
                throw new ItemNotFoundException();
            }
            return commentList;

        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

    public Comment getCommentById(final Long commentId)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final Comment comment = commentDao.getById(commentId);
            if (comment == null) {
                throw new ItemNotFoundException();
            }
            return comment;

        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

    public Comment createComment(final Long userId, final Long postId, final Long musicId, final String commentBody)
        throws DuplicateItemException, ItemNotFoundException, StorageServiceException {

        try {
            final Date timeNow = new Date();
            final Long commentId = commentDao.create(userId, postId, musicId, commentBody, Constant.COMMENT_STATUS_ACTIVE, timeNow, timeNow);
            return getCommentById(commentId);

        } catch (final DBIException dbiException) {
            if (CommonUtils.isDuplicateEntryException(dbiException)) {
                throw new DuplicateItemException(dbiException);
            }
            throw new StorageServiceException(dbiException);
        }
    }

    public Comment updateCommentById(final Long commentId, final Long musicId, final String commentBody)
        throws ItemNotFoundException, StorageServiceException {

            try {
                final Date timeNow = new Date();
                final Integer changes = commentDao.update(commentId, musicId, commentBody, timeNow);
                if (changes == 0) {
                    throw new ItemNotFoundException();
                }
                return getCommentById(commentId);
            } catch (final DBIException dbiException) {
                throw new StorageServiceException(dbiException);
            }
    }

    public void deleteCommentById(final Long commentId) {

        commentDao.delete(commentId);
    }
}
