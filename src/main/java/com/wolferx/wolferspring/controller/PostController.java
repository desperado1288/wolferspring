package com.wolferx.wolferspring.controller;

import com.alibaba.fastjson.JSONObject;
import com.wolferx.wolferspring.common.CommonUtil;
import com.wolferx.wolferspring.common.annotation.LoggedUser;
import com.wolferx.wolferspring.common.exception.BaseException;
import com.wolferx.wolferspring.config.RouteConfig;
import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.entity.User;
import com.wolferx.wolferspring.service.PostService;
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
@RequestMapping(value = RouteConfig.POST_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Post> getAllPost()
        throws BaseException {

        logger.info("Start getAllPost()");
        final List<Post> posts = postService.findAll(false);

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
    public Post createPost(@RequestBody JSONObject requestBody,  @LoggedUser User user)
        throws BaseException {

        logger.info("Start createPost()");
        final Long userId = requestBody.getInteger("userId").longValue();
        final String title = requestBody.getString("postTitle");
        final String body = requestBody.getString("postBody");
        final String postCoverUrl = requestBody.getString("postCoverUrl");
        final String musicIds = requestBody.getString("musicIds");
        final Integer type = CommonUtil.isNullEmpty(musicIds) ? Post.TYPE_TEXT_POST : Post.TYPE_MUSIC_POST;
        final String slug = requestBody.getString("slug");
        final String tag = requestBody.getString("tag");

        Post post = postService.createPost(userId, title, body, postCoverUrl, type, musicIds, slug, tag);
        logger.info("End createPost()");
        return post;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Post updatePost(@RequestBody JSONObject requestBody)
        throws BaseException {

        logger.info("Initialize updatePost()");

        final JSONObject requestParams = requestBody.getJSONObject("post");

        final Long postId = requestParams.getLong("post_id");
        String title = requestParams.getString("post_title");
        String body = requestParams.getString("post_body");
        String postCoverUrl = requestParams.getString("post_cover_url");
        String musicIds = requestParams.getString("music_ids");
        String slug = requestParams.getString("slug");
        String tag = requestParams.getString("tag");

        final Post p = postService.findById(postId);
        if(title != null && title.isEmpty()) {
            title = p.getPost_title();
        }
        if(body != null && body.isEmpty()) {
            body = p.getPost_body();
        }
        if(postCoverUrl != null && postCoverUrl.isEmpty()) {
            postCoverUrl = p.getPost_cover_url();
        }
        if(musicIds != null && musicIds.isEmpty()) {
            musicIds = p.getMusic_ids();
        }
        if(slug != null && slug.isEmpty()) {
            slug = p.getSlug();
        }
        if(tag != null && tag.isEmpty()) {
            tag = p.getTag();
        }
        Integer type = CommonUtil.isNullEmpty(musicIds) ? Post.TYPE_TEXT_POST : Post.TYPE_MUSIC_POST;

        logger.info("Start updatePost() by postId: " + postId);
        Post post = postService.updateById(postId, title, body, postCoverUrl, type, musicIds, slug, tag);
        logger.info("End updatePost() by postId: " + postId);

        return post;
    }

    @RequestMapping(value="/{postId}", method = RequestMethod.DELETE)
    public String deletePost(@PathVariable("postId") Long postId)
        throws BaseException {
        logger.info("Start deletePost() for postId: " + postId);

        postService.deleteById(postId);
        logger.info("End deletePost() for postId: " + postId);
        return "deleted successfully";
    }
}
