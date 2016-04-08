package com.wolferx.wolferspring.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.wolferx.wolferspring.common.response.TokenResponse;
import com.wolferx.wolferspring.config.RouteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthMainFilter extends GenericFilterBean {

    public final static String TOKEN_SESSION_KEY = "token";
    public final static String USER_SESSION_KEY = "user";

    private final static Logger logger = LoggerFactory.getLogger(AuthMainFilter.class);

    private AuthenticationManager authManager;

    public AuthMainFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final String resourcePath = new UrlPathHelper().getPathWithinApplication(request);
        final Optional<String> inputEmail = Optional.ofNullable(request.getHeader("X-Auth-Username"));
        final Optional<String> inputPassword = Optional.ofNullable(request.getHeader("X-Auth-Password"));
        final Optional<String> inputToken = Optional.ofNullable(request.getHeader("X-Auth-Token"));

        try {
            /***********
             * call: username password authentication
             * when: post request to /api/v1/auth
             ***********/
            if (RouteConfig.AUTH_URL.equalsIgnoreCase(resourcePath) && request.getMethod().equals("POST")) {

                final String email = inputEmail.orElseThrow(() -> new BadCredentialsException("Invalid User Credentials"));
                final String password = inputPassword.orElseThrow(() -> new BadCredentialsException("Invalid User Credentials"));

                logger.debug("Trying to authenticate user: {}", email);
                final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
                final Authentication authResult = authManager.authenticate(authRequest);
                if (authResult == null || !authResult.isAuthenticated()) {
                    throw new AuthenticationServiceException("Unable to authenticate User with provided credentials");
                }
                logger.debug("User successfully authenticated");

                SecurityContextHolder.getContext().setAuthentication(authResult);

                final TokenResponse tokenResponse = new TokenResponse(authResult.getDetails().toString());
                final String tokenJsonResponse = new ObjectMapper().writeValueAsString(tokenResponse);

                response.setStatus(HttpServletResponse.SC_OK);
                response.addHeader("Content-Type", "application/json");
                response.getWriter().print(tokenJsonResponse);
                return;
            }

            /**********
             / call: JWT authentication
             / when: token is presented in header
             ***********/
            if (inputToken.isPresent()) {

                final String token = inputToken.orElseThrow(() -> new BadCredentialsException("Invalid User Credentials"));

                logger.debug("Trying to authenticate user by X-Auth-Token method. Token: {}", token);
                PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
                Authentication authResult = authManager.authenticate(requestAuthentication);
                if (authResult == null || !authResult.isAuthenticated()) {
                    throw new InternalAuthenticationServiceException("Unable to authenticate User for provided credentials");
                }
                logger.debug("User successfully authenticated");

                SecurityContextHolder.getContext().setAuthentication(authResult);
            }

            /**********
             / if neither password and token are presented
             / then pass request down to the filter chain
             ***********/
            logger.debug("AuthMainFilter is passing request down the filter chain");
            addSessionContextToLogging();
            chain.doFilter(req, res);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            logger.error("Internal authentication service exception", internalAuthenticationServiceException);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        } finally {
            MDC.remove(TOKEN_SESSION_KEY);
            MDC.remove(USER_SESSION_KEY);
        }
    }

    private void addSessionContextToLogging() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getDetails().toString())) {
            MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-1");
            tokenValue = encoder.encodePassword(authentication.getDetails().toString(), "not_so_random_salt");
        }
        MDC.put(TOKEN_SESSION_KEY, tokenValue);

        String userValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getPrincipal().toString())) {
            userValue = authentication.getPrincipal().toString();
        }
        MDC.put(USER_SESSION_KEY, userValue);
    }
}
