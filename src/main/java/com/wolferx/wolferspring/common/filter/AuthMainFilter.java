package com.wolferx.wolferspring.common.filter;

import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.security.JWTAuthRefreshToken;
import com.wolferx.wolferspring.common.security.JWTAuthToken;
import com.wolferx.wolferspring.common.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

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

    private AuthenticationManager authenticationManager;

    public AuthMainFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException, AuthenticationException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final Optional<String> inputToken = Optional.ofNullable(request.getHeader(Constant.AUTH_JWT_TOKEN_HEADER));
        final Optional<String> inputRefreshToken = Optional.ofNullable(request.getHeader(Constant.AUTH_JWT_REFRESH_TOKEN_HEADER));

        try {
            /**
             * call: username password authentication
             * when: post request to /api/v1/auth
             * deprecated: using /api/v1/auth/login to authenticate user
             */
            /*
            final String resourcePath = new UrlPathHelper().getPathWithinApplication(request);
            final Optional<String> inputEmail = Optional.ofNullable(request.getHeader(Constant.AUTH_USERNAME_HEADER));
            final Optional<String> inputPassword = Optional.ofNullable(request.getHeader(Constant.AUTH_PASSWORD_HEADER));
            if (RouteConfig.AUTH_URL.equalsIgnoreCase(resourcePath) && request.getMethod().equals("POST")) {

                final String email = inputEmail.orElseThrow(() -> new BadCredentialsException("Invalid User Credentials"));
                final String password = inputPassword.orElseThrow(() -> new BadCredentialsException("Invalid User Credentials"));

                logger.info("<Start> Authenticate user with password. User: {}", email);
                final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
                final Authentication authentication = authenticationManager.authenticate(authRequest);
                if (authentication == null || !authentication.isAuthenticated()) {
                    logger.error("<In> Failed to authenticate User with password. User: {}", email);
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
            */

            /**
             * call: JWT authentication
             * when: token is presented in header
             */
            if (inputToken.isPresent()) {

                logger.debug("<Start> Authenticate user with token");
                final String token = inputToken.get();
                final JWTAuthToken authRequest = new JWTAuthToken(token, null);
                final Authentication authentication = authenticationManager.authenticate(authRequest);
                if (authentication == null || !authentication.isAuthenticated()) {
                    logger.error("<In> Failed to authenticate User with token");
                    throw new AuthenticationServiceException("Unable to authenticate User for provided credentials");
                }
                logger.debug("<End> Authenticate user with token");
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else if (inputRefreshToken.isPresent()) {

                logger.debug("<Start> Authenticate user with refresh token");
                final String refreshToken = inputRefreshToken.get();
                final JWTAuthRefreshToken authRequest = new JWTAuthRefreshToken(refreshToken, null);
                final Authentication authentication = authenticationManager.authenticate(authRequest);
                if (authentication == null || !authentication.isAuthenticated()) {
                    logger.error("<In> Failed to authenticate User with token");
                    throw new AuthenticationServiceException("Unable to authenticate User for provided credentials");
                }
                logger.debug("<End> Authenticate user with refresh token");
                SecurityContextHolder.getContext().setAuthentication(authentication);

                CommonUtils.addCookie(response, Constant.AUTH_JWT_TOKEN_COOKIE, authentication.getCredentials().toString(), Constant.AUTH_JWT_TOKEN_EXPIRE);
            }

            /**********
             / if: neither password and token are presented
             / then: pass request down to the filter chain
             ***********/
            logger.debug("<In> Passing request down the filter chain");
            chain.doFilter(req, res);

        } catch (final AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            logger.error("<In> Authentication Exception", authenticationException);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
    }

}
