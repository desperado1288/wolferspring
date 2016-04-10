package com.wolferx.wolferspring.controller;


import com.alibaba.fastjson.JSONObject;
import com.wolferx.wolferspring.common.exception.BaseServiceException;
import com.wolferx.wolferspring.entity.Comment;
import com.wolferx.wolferspring.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/comment", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Comment> getAllComment() throws BaseServiceException {

        logger.info("Start getAllComment()");

        List<Comment> comments = commentService.findAll(false);

        logger.info("End getAllComment()");

        return comments;
    }

    @RequestMapping(value = "/{post_id}", method = RequestMethod.GET)
    public List<Comment> getCommentByPostId(@PathVariable("post_id") Long post_id)
        throws BaseServiceException {

        logger.info("Start getCommentByPostId for postId: " + post_id);

        List<Comment> clist = commentService.findByPostId(post_id);

        logger.info("End getCommentByPostId for postId: " + post_id);

        return clist;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Comment createComment(@RequestBody JSONObject requestBody)
        throws BaseServiceException {

        logger.info("Start createComment()");

        final JSONObject requestParams = requestBody.getJSONObject("comment");
        final Long user_id = requestParams.getLong("user_id");
        final Long post_id = requestParams.getLong("post_id");
        final Long music_id = requestParams.getLong("music_id");
        final String body = requestParams.getString("comment_body");

        Comment c = commentService.createComment(user_id, post_id, music_id, body);

        logger.info("End createComment()");
        return c;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Comment updateComment(@RequestBody JSONObject requestBody)
        throws BaseServiceException {

        logger.info("Initilize updateComment()");

        final JSONObject requestParams = requestBody.getJSONObject("comment");
        final Long comment_id = requestParams.getLong("comment_id");
        final Comment c = commentService.findByCommentId(comment_id);
        Long music_id = requestParams.getLong("music_id");
        String body = requestParams.getString("comment_body");

        if(music_id != null && music_id == -1) {
            music_id = c.getMusic_id() == 0 ? null : c.getMusic_id();
        }
        if(body != null && body.isEmpty()) {
            body = c.getComment_body();
        }

        logger.info("Start updateComment() for comment_id: " + comment_id);
        Comment comment = commentService.updateById(comment_id, music_id, body);

        logger.info("End updateComment() for comment_id: " + comment_id);

        return comment;
    }

    @RequestMapping(value = "/{comment_id}", method = RequestMethod.DELETE)
    public String deleteComment(@PathVariable("comment_id") Long comment_id)
        throws BaseServiceException {

        logger.info("Start deleteComment() for comment_id: " + comment_id);

        commentService.deleteById(comment_id);

        logger.info("End deleteComment() for comment_id: " + comment_id);
        return "deleted successfully";
    }
}
