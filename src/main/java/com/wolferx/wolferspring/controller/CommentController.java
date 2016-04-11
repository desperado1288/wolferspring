package com.wolferx.wolferspring.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.constant.ErrorCode;
import com.wolferx.wolferspring.common.exception.BaseServiceException;
import com.wolferx.wolferspring.common.exception.InvalidRequestInputException;
import com.wolferx.wolferspring.common.exception.NoSuchItemException;
import com.wolferx.wolferspring.config.RouteConfig;
import com.wolferx.wolferspring.entity.Comment;
import com.wolferx.wolferspring.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping(value = RouteConfig.COMMENT_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private CommentService commentService;

    @Autowired
    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Comment> getAllComment()
        throws BaseServiceException {

        logger.info("<Start> getAllComment()");
        final List<Comment> comments = commentService.getAllComment(Constant.COMMENT_STATUS_ACTIVE);
        logger.info("<End> getAllComment()");

        return comments;
    }

    @RequestMapping(value = "/{commentId}", method = RequestMethod.GET)
    public Comment getCommentById(@PathVariable("commentId") final Long commentId)
        throws BaseServiceException {

        logger.info("<Start> getPostById(): PostId: {}", commentId);
        final Comment comment =  commentService.getCommentById(commentId)
            .orElseThrow(() -> new NoSuchItemException(String.format("<In> getCommentById(): Not found: commentId: %s", commentId), ErrorCode.ITEM_NOT_FOUND));
        logger.info("<End> getPostById(): PostId: {}", commentId);

        return comment;
    }

    @RequestMapping(value = "/post/{postId}", method = RequestMethod.GET)
    public List<Comment> getCommentByPostId(@PathVariable("postId") final Long postId)
        throws BaseServiceException {

        logger.info("<Start> getCommentByPostId(): PostId: {}", postId);
        final List<Comment> comments = commentService.getCommentByPostId(postId, Constant.COMMENT_STATUS_ACTIVE)
            .orElseThrow(() -> new NoSuchItemException(String.format("<In> getCommentByPostId(): Not found: PostId: %s", postId), ErrorCode.ITEM_NOT_FOUND));
        logger.info("<End> getCommentByPostId(): PostId: {}", postId);

        return comments;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Comment createComment(@RequestBody final JsonNode requestBody)
        throws BaseServiceException {

        logger.info("<Start> createComment()");
        // required input
        final Long userId;
        final Long postId;
        final String commentBody;
        try {
            userId = requestBody.get("postTitle").asLong();
            postId = requestBody.get("postBody").asLong();
            commentBody = requestBody.get("commentBody").asText();
        } catch (final NullPointerException nullPointerException) {
            logger.error("<In> createPost(): Missing required input", nullPointerException);
            throw new InvalidRequestInputException("Missing required input");
        }

        // optional input
        final Long musicId = requestBody.has("musicId") ? requestBody.get("musicId").asLong() : null;

        final Comment comment = commentService.createComment(userId, postId, musicId, commentBody);
        logger.info("<End> createComment()");

        return comment;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Comment updateComment(@RequestBody final JsonNode requestBody)
        throws BaseServiceException {

        final Long commentId;
        try {
            commentId = requestBody.get("commentId").asLong();
        } catch (final NullPointerException nullPointerException) {
            logger.error("Missing required input", nullPointerException);
            throw new InvalidRequestInputException("Missing required input");
        }

        final Comment baseComment = commentService.getCommentById(commentId)
            .orElseThrow(() -> new NoSuchItemException(String.format("<In> updatePost(): Not found: CommentId: %s", commentId)));

        final String body = requestBody.has("commentBody") ? requestBody.get("commentBody").asText() : baseComment.getCommentBody();
        final Long musicId = requestBody.has("musicId") ? requestBody.get("musicId").asLong() : baseComment.getMusicId();

        logger.info("<Start> updateComment(): CommentId: {}", commentId);
        final Comment comment = commentService.updateCommentById(commentId, musicId, body);
        logger.info("<End> updateComment(): CommentId: {}", commentId);

        return comment;
    }

    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    public Boolean deleteComment(@PathVariable("commentId") final Long commentId)
        throws BaseServiceException {

        logger.info("<Start> deleteComment(): CommentId: " + commentId);
        commentService.deleteCommentById(commentId);
        logger.info("<End> deleteComment(): CommentId: " + commentId);

        return true;
    }
}
