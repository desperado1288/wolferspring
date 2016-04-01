package com.wolferx.wolferspring.common.filter;

import com.google.common.base.Optional;
import com.wolferx.wolferspring.config.RouteConfig;
import com.wolferx.wolferspring.external.BackendAdminUsernamePasswordAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ManagementEndpointAuthenticationFilter extends GenericFilterBean {

    private final static Logger logger = LoggerFactory.getLogger(ManagementEndpointAuthenticationFilter.class);
    private AuthenticationManager authenticationManager;
    private Set<String> managementEndpoints;

    public ManagementEndpointAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        prepareManagementEndpointsSet();
    }

    private void prepareManagementEndpointsSet() {
        managementEndpoints = new HashSet<>();
        managementEndpoints.add(RouteConfig.AUTOCONFIG_ENDPOINT);
        managementEndpoints.add(RouteConfig.BEANS_ENDPOINT);
        managementEndpoints.add(RouteConfig.CONFIGPROPS_ENDPOINT);
        managementEndpoints.add(RouteConfig.ENV_ENDPOINT);
        managementEndpoints.add(RouteConfig.MAPPINGS_ENDPOINT);
        managementEndpoints.add(RouteConfig.METRICS_ENDPOINT);
        managementEndpoints.add(RouteConfig.SHUTDOWN_ENDPOINT);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Optional<String> username = Optional.fromNullable(request.getHeader("X-Auth-Username"));
        Optional<String> password = Optional.fromNullable(request.getHeader("X-Auth-Password"));

        String resourcePath = new UrlPathHelper().getPathWithinApplication(request);

        try {
            if (postToManagementEndpoints(resourcePath)) {
                logger.debug("Trying to authenticate user {} for management endpoint by X-Auth-Username method", username);
                processManagementEndpointUsernamePasswordAuthentication(username, password);
            }

            logger.debug("ManagementEndpointAuthenticationFilter is passing request down the filter chain");
            chain.doFilter(req, res);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
    }

    private boolean postToManagementEndpoints(String resourcePath) {
        return managementEndpoints.contains(resourcePath);
    }

    private void processManagementEndpointUsernamePasswordAuthentication(Optional<String> username, Optional<String> password) throws IOException {
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    private Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password) {
        BackendAdminUsernamePasswordAuthenticationToken requestAuthentication = new BackendAdminUsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Backend Admin for provided credentials");
        }
        logger.debug("Backend Admin successfully authenticated");
        return responseAuthentication;
    }
}
