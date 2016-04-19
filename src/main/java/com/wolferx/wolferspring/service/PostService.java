package com.wolferx.wolferspring.service;

import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.exception.DuplicateItemException;
import com.wolferx.wolferspring.common.exception.ItemNotFoundException;
import com.wolferx.wolferspring.common.exception.StorageServiceException;
import com.wolferx.wolferspring.common.utils.CommonUtils;
import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.jdbi.dao.PostDao;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostDao postDao;

    @Autowired
    public PostService(final PostDao postDao) {
        this.postDao = postDao;
    }

    public List<Post> getAllPost(final Integer status)
    throws ItemNotFoundException, StorageServiceException {
        try {
            final List<Post> postList = postDao.getAll(status);
            if (postList.isEmpty()) {
                throw new ItemNotFoundException();
            }
            return postList;

        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

    public Post getPostById(final Long postId)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final Post post = postDao.getById(postId);
            if (post == null) {
                throw new ItemNotFoundException();
            }
            return post;

        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

    public Post createPost(final Long userId, final String title, final String body, final String postCoverUrl,
                           final Integer type, final String musicIds, final String slug, final String tag)
        throws DuplicateItemException, ItemNotFoundException, StorageServiceException{

        try {
            final Date timeNow = new Date();
            final Long postId = postDao.create(userId, title, body, postCoverUrl,
                type, musicIds, slug, tag, Constant.POST_STATUS_ACTIVE, timeNow, timeNow);
            return getPostById(postId);

        } catch (final DBIException dbiException) {
            if (CommonUtils.isDuplicateEntryException(dbiException)) {
                throw new DuplicateItemException(dbiException);
            }
            throw new StorageServiceException(dbiException);
        }
    }

    public Post updatePostById(final Long postId, final String title, final String body, final String postCoverUrl,
                               final Integer type, final String musicIds, final String slug, final String tag)
        throws ItemNotFoundException, StorageServiceException{

        try {
            final Date timeNow = new Date();
            final Integer changes = postDao.update(postId, title, body, postCoverUrl, type, musicIds, slug, tag, timeNow);
            if (changes == 0) {
                throw new ItemNotFoundException();
            }
            return getPostById(postId);
        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

    public void deletePostById(final Long postId)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final Integer changes = postDao.delete(postId);
            if (changes == 0) {
                throw new ItemNotFoundException();
            }
        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

}
