package com.wolferx.wolferspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.wolferx.wolferspring.common.exception.BaseServiceException;
import com.wolferx.wolferspring.common.exception.InvalidInputException;
import com.wolferx.wolferspring.common.response.TokenResponse;
import com.wolferx.wolferspring.config.RouteConfig;
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
    public TokenResponse registerUser(@RequestBody JsonNode requestJson)
        throws IOException, BaseServiceException {

        // valid input
        final String email;
        final String password;
        try {
            email = requestJson.get("email").asText();
            password = requestJson.get("password").asText();
        } catch (NullPointerException nullPointerException) {
            logger.error("<In> registerUser() : get invalid credentials", nullPointerException);
            throw new InvalidInputException("Invalid user provided credentials");
        }

        // register user
        logger.info("<Start> registerUser() for User: {} ", email);
        final Authentication authentication = authService.registerUser(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("End registerUser() for User: {}", email);

        return new TokenResponse(authentication.getDetails().toString());
    }
}
