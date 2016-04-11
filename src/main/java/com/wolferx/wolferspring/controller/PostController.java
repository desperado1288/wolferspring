package com.wolferx.wolferspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.wolferx.wolferspring.common.annotation.LoggedUser;
import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.constant.ErrorCode;
import com.wolferx.wolferspring.common.exception.BaseServiceException;
import com.wolferx.wolferspring.common.exception.InvalidRequestInputException;
import com.wolferx.wolferspring.common.exception.NoSuchItemException;
import com.wolferx.wolferspring.common.utils.CommonUtils;
import com.wolferx.wolferspring.config.RouteConfig;
import com.wolferx.wolferspring.entity.Post;
import com.wolferx.wolferspring.entity.User;
import com.wolferx.wolferspring.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
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
    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Post> getAllPost()
        throws BaseServiceException {

        logger.info("<Start> getAllPost()");
        final List<Post> posts = postService.getAllPost(Constant.POST_STATUS_ACTIVE);
        logger.info("<End> getAllPost()");

        return posts;
    }

    @RequestMapping(value = "/{postId}", method = RequestMethod.GET)
    public Post getPostById(@PathVariable("postId") final Long postId)
        throws BaseServiceException {

        logger.info("<Start> getPostById(): PostId: {}", postId);
        final Post post =  postService.getPostById(postId)
            .orElseThrow(() -> new NoSuchItemException(String.format("<In> getPostById(): Not found: PostId: %s", postId), ErrorCode.ITEM_NOT_FOUND));
        logger.info("<End> getPostById(): PostId: {}", postId);

        return post;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Post createPost(@RequestBody final JsonNode requestBody, @LoggedUser final User user)
        throws BaseServiceException {

        logger.info("<Start> createPost()");
        // required input
        final String title;
        final String body;
        try {
            title = requestBody.get("postTitle").asText();
            body = requestBody.get("postBody").asText();
        } catch (final NullPointerException nullPointerException) {
            logger.error("<In> createPost(): Missing required input", nullPointerException);
            throw new InvalidRequestInputException("Missing required input");
        }

        // optional input
        final String postCoverUrl = requestBody.has("postCoverUrl") ? requestBody.get("postCoverUrl").asText() : null;
        final String musicIds = requestBody.has("musicIds") ? requestBody.get("musicIds").asText() : null;
        final Integer type = StringUtils.isEmpty(musicIds) ? Constant.POST_TYPE_TEXT : Constant.POST_TYPE_MUSIC;
        final String slug = requestBody.has("slug") ? requestBody.get("slug").asText() : null;
        final String tag = requestBody.has("tag") ? requestBody.get("tag").asText() : null;

        final Post post = postService.createPost(user.getUserId(), title, body, postCoverUrl, type, musicIds, slug, tag);
        logger.info("<End> createPost()");

        return post;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Post updatePost(@RequestBody final JsonNode requestBody)
        throws BaseServiceException {
        final Long postId;
        try {
            postId = requestBody.get("postId").asLong();
        } catch (final NullPointerException nullPointerException) {
            logger.error("<In> updatePost(): Missing required input", nullPointerException);
            throw new InvalidRequestInputException("Missing required input");
        }

        logger.info("<Start> updatePost(): PostId: {}", postId);
        final Post basePost = postService.getPostById(postId)
            .orElseThrow(() -> new NoSuchItemException(String.format("<In> updatePost(): Not found: PostId: %s", postId)));

        final String title = requestBody.has("postTitle") ? requestBody.get("postTitle").asText() : basePost.getPostTitle();
        final String body = requestBody.has("postBody") ? requestBody.get("postBody").asText() : basePost.getPostBody();
        final String postCoverUrl = requestBody.has("postCoverUrl") ? requestBody.get("postCoverUrl").asText() : basePost.getPostCoverUrl();
        final String musicIds = requestBody.has("musicIds") ? requestBody.get("musicIds").asText() : basePost.getMusicIds();
        final Integer type = StringUtils.isEmpty(musicIds) ? Constant.POST_TYPE_TEXT : Constant.POST_TYPE_MUSIC;
        final String slug = requestBody.has("slug") ? requestBody.get("slug").asText() : basePost.getSlug();
        final String tag = requestBody.has("tag") ? requestBody.get("tag").asText() : basePost.getTag();

        final Post post = postService.updatePostById(postId, title, body, postCoverUrl, type, musicIds, slug, tag);
        logger.info("<End> updatePost(): PostId: {}", postId);

        return post;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Boolean deletePost(@RequestBody final JsonNode requestBody)
        throws BaseServiceException {

        final Long postId = (Long) CommonUtils.parserJsonNode("postId", requestBody, Long.class, logger);

        logger.info("<Start> deletePost(): postId: {}", postId);
        postService.deletePostById(postId);
        logger.info("<End> deletePost(): postId: {}", postId);

        return true;
    }
}
