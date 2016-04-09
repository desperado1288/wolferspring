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

    private final static Logger logger = LoggerFactory.getLogger(AuthMainFilter.class);

    public final static String TOKEN_SESSION_KEY = "token";
    public final static String USER_SESSION_KEY = "user";

    private AuthenticationManager authManager;

    public AuthMainFilter(final AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException, AuthenticationException {

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

                logger.info("<Start> Authenticate user with password. User: {}", email);
                final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
                final Authentication authentication = authManager.authenticate(authRequest);
                if (authentication == null || !authentication.isAuthenticated()) {
                    logger.error("<In> AuthMainFilter failed to authenticate User with password. User: {}", email);
                    throw new AuthenticationServiceException("Unable to authenticate User with provided credentials");
                }
                logger.info("<End> Authenticated with password. User: {}", email);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                final TokenResponse tokenResponse = new TokenResponse(authentication.getDetails().toString());
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

                logger.debug("<Start> Authenticate user with token");
                final String token = inputToken.get();
                final PreAuthenticatedAuthenticationToken authRequest = new PreAuthenticatedAuthenticationToken(token, null);
                final Authentication authentication = authManager.authenticate(authRequest);
                if (authentication == null || !authentication.isAuthenticated()) {
                    logger.error("<In> AuthMainFilter failed to authenticate User with token");
                    throw new AuthenticationServiceException("Unable to authenticate User for provided credentials");
                }
                logger.debug("<End> Authenticate user with token");

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // add context to Mapped Diagnostic Context log
            addSessionContextToLogging();

            /**********
             / if: neither password and token are presented
             / then: pass request down to the filter chain
             ***********/
            logger.debug("AuthMainFilter is passing request down the filter chain");
            chain.doFilter(req, res);

        } catch (AuthenticationServiceException authenticationServiceException) {
            SecurityContextHolder.clearContext();
            logger.error("Authentication service exception", authenticationServiceException);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationServiceException.getMessage());
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            logger.error("Authentication exception", authenticationException);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        } finally {
            MDC.remove(TOKEN_SESSION_KEY);
            MDC.remove(USER_SESSION_KEY);
        }
    }

    private void addSessionContextToLogging() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getDetails().toString())) {
            final MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-1");
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
