package com.wolferx.wolferspring.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.wolferx.wolferspring.common.response.TokenResponse;
import com.wolferx.wolferspring.config.RouteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
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

public class AuthenticationFilter extends GenericFilterBean {

    public final static String TOKEN_SESSION_KEY = "token";
    public final static String USER_SESSION_KEY = "user";

    private AuthenticationManager authenticationManager;
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Optional<String> username = Optional.fromNullable(request.getHeader("X-Auth-Username"));
        Optional<String> password = Optional.fromNullable(request.getHeader("X-Auth-Password"));
        Optional<String> token = Optional.fromNullable(request.getHeader("X-Auth-Token"));

        String resourcePath = new UrlPathHelper().getPathWithinApplication(request);

        try {

            /**********
             / if username and password is presented
             / then process username password authentication
             ***********/
            if (RouteConfig.AUTHENTICATE_URL.equalsIgnoreCase(resourcePath) && request.getMethod().equals("POST")) {

                logger.debug("Trying to authenticate user {} by X-Auth-Username method", username);

                Authentication authResult = authWithUserPassword(username, password);
                logger.debug("User successfully authenticated");

                SecurityContextHolder.getContext().setAuthentication(authResult);

                TokenResponse tokenResponse = new TokenResponse(authResult.getDetails().toString());
                String tokenJsonResponse = new ObjectMapper().writeValueAsString(tokenResponse);

                response.setStatus(HttpServletResponse.SC_OK);
                response.addHeader("Content-Type", "application/json");
                response.getWriter().print(tokenJsonResponse);
                return;
            }

            /**********
             / if token is presented
             / then process token authentication
             ***********/
            if (token.isPresent()) {

                logger.debug("Trying to authenticate user by X-Auth-Token method. Token: {}", token);

                Authentication authResult = authWithToken(token);
                logger.debug("User successfully authenticated");

                SecurityContextHolder.getContext().setAuthentication(authResult);
            }

            /**********
             / if neither password and token are presented
             / then pass request down to the filter chain
             ***********/
            logger.debug("AuthenticationFilter is passing request down the filter chain");
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

    private Authentication authWithUserPassword(Optional<String> username, Optional<String> password)
        throws IOException {

        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authResult = authenticationManager.authenticate(requestAuthentication);
        if (authResult == null || !authResult.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate User for provided credentials");
        }
        return authResult;
    }

    private Authentication authWithToken(Optional<String> token) {

        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        Authentication authResult = authenticationManager.authenticate(requestAuthentication);
        if (authResult == null || !authResult.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate User for provided credentials");
        }
        return authResult;
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
