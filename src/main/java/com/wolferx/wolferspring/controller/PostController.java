package com.wolferx.wolferspring.controller;

import com.alibaba.fastjson.JSONObject;
import com.wolferx.wolferspring.common.exception.BaseException;
import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.service.PostService;
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
@RequestMapping(value = "/api/v1/post", produces = {MediaType.APPLICATION_JSON_VALUE})
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Post> getAllPost()
        throws BaseException {

        logger.info("Start getAllPost()");

        final List<Post> posts = postService.findAll();

        logger.info("End getAllPost()");

        return posts;
    }

    @RequestMapping(value = "/{postId}", method = RequestMethod.GET)
    public Post getPostByUserId(@PathVariable("postId") Long postId)
        throws BaseException {

        logger.info("Start getPostById() for postId: " + postId);

        final Post post =  postService.findById(postId);

        logger.info("End getPostByUserId() for postId: " + postId);
        return post;
    }

    /*
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> getPostByUserId(@RequestParam("userId") Long userId) {

        logger.info("Start getPostByUserId() for userId: " + userId);

        List<Post> posts =  postService.findByUserId(userId);

        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("message", "get all posts by userId: " + userId);
        response.put("posts", posts);

        logger.info("End getPostByUserId() for userId: " + userId);
        return response;
    }
    */

    @RequestMapping(method = RequestMethod.POST)
    public Post createPost(@RequestBody JSONObject requestBody)
        throws BaseException {
        logger.info("Start createPost()");
        final JSONObject requestParams = requestBody.getJSONObject("post");
        final Long userId = requestParams.getLong("userId");
        final String title = requestParams.getString("post_title");
        final String body = requestParams.getString("post_body");
        final String postCoverUrl = requestParams.getString("post_cover_url");
        final String musicIds = requestParams.getString("music_ids");
        final Integer type = musicIds.equals("") ? 0 : 1;
        final String slug = requestParams.getString("slug");
        final String tag = requestParams.getString("tag");

        Post post = postService.createPost(userId, title, body, postCoverUrl, type, musicIds, slug, tag);
        logger.info("End createPost()");
        return post;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Post updatePost(@RequestBody JSONObject requestBody)
        throws BaseException {
        logger.info("Start updatePost()");
        final JSONObject requestParams = requestBody.getJSONObject("post");
        final Long postId = requestParams.getLong("postId");
        final String title = requestParams.getString("post_title");
        final String body = requestParams.getString("post_body");
        final String postCoverUrl = requestParams.getString("post_cover_url");
        final String musicIds = requestParams.getString("music_ids");
        final Integer type = musicIds.equals("") ? 0 : 1;
        final String slug = requestParams.getString("slug");
        final String tag = requestParams.getString("tag");
        logger.info("Start updatePost() by postId: " + postId);
        Post post = postService.createPost(postId, title, body, postCoverUrl, type, musicIds, slug, tag);

        return post;
    }
}
