package com.wolferx.wolferspring.controller;

import com.alibaba.fastjson.JSONObject;
import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/post")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Post> getAllPost() {
        return postService.findAll();
    }

    @RequestMapping(value = "/{postId}", method = RequestMethod.GET)
    public Map<String, Object> getPostByUserId(@PathVariable("postId") Long postId) {

        logger.info("Start getPostById() for postId: " + postId);

        Post post =  postService.findById(postId);

        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("message", "get post by postId: " + postId);
        response.put("post", post);

        logger.info("End getPostByUserId() for postId: " + postId);
        return response;
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
    public Map<String, Object> createPost(@RequestBody JSONObject requestBody) {

        JSONObject requestParams = requestBody.getJSONObject("post");
        Long userId = requestParams.getLong("userId");
        String slug = requestParams.getString("slug");
        String title = requestParams.getString("title");
        String tag = requestParams.getString("tag");
        String body = requestParams.getString("body");

        Post post = postService.createPost(userId, slug, title, tag, body);

        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("message", "post has been added successfully");
        response.put("post", post);
        return response;
    }

}
