package com.wolferx.wolferspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.exception.BaseServiceException;
import com.wolferx.wolferspring.common.response.TokenResponse;
import com.wolferx.wolferspring.common.utils.CommonUtils;
import com.wolferx.wolferspring.config.RouteConfig;
import com.wolferx.wolferspring.entity.Token;
import com.wolferx.wolferspring.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = RouteConfig.AUTH_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public TokenResponse registerUser(@RequestBody final JsonNode requestBody)
        throws IOException, BaseServiceException {

        // required input
        final String email = (String) CommonUtils.parserJsonNode("email", requestBody, String.class, logger);
        final String password = (String) CommonUtils.parserJsonNode("password", requestBody, String.class, logger);

        // register user
        logger.info("<Start> registerUser(): for User: {} ", email);
        final Authentication authentication = authService.registerUser(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("<End> registerUser(): for User: {}", email);

        return new TokenResponse((Token) authentication.getDetails());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public TokenResponse loginUser(@RequestBody final JsonNode requestBody, final HttpServletResponse response)
        throws IOException, BaseServiceException {

        // required input
        final String email = (String) CommonUtils.parserJsonNode("email", requestBody, String.class, logger);
        final String password = (String) CommonUtils.parserJsonNode("password", requestBody, String.class, logger);
        logger.info("<Start> loginUser(): for User: {} ", email);
        // optional input
        final Boolean rememberMe = requestBody.has("rememberMe") ? requestBody.get("rememberMe").asBoolean() : false;
        // register user
        final Authentication authentication = authService.authByPassword(email, password, rememberMe);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final Token token = (Token) authentication.getDetails();

        // set cookies
        if (token.hasRefreshToken()) {
            CommonUtils.addCookie(response, Constant.AUTH_JWT_TOKEN_COOKIE, token.getToken(), Constant.AUTH_JWT_TOKEN_EXPIRE);
            CommonUtils.addCookie(response, Constant.AUTH_JWT_REFRESH_TOKEN_COOKIE, token.getRefreshToken(), Constant.AUTH_JWT_REFRESH_TOKEN_EXPIRE);
        } else {
            CommonUtils.addCookie(response, Constant.AUTH_JWT_TOKEN_COOKIE, token.getToken(), -1);
        }

        logger.info("<End> loginUser(): for User: {}", email);
        return new TokenResponse(token);
    }
}
