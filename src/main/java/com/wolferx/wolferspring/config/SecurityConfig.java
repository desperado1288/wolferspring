package com.wolferx.wolferspring.config;

import com.wolferx.wolferspring.common.constant.Constant;
import com.wolferx.wolferspring.common.filter.AuthMainFilter;
import com.wolferx.wolferspring.common.security.JWTRefreshTokenAuthProvider;
import com.wolferx.wolferspring.common.security.JWTTokenAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableScheduling
@Order(ManagementServerProperties.BASIC_AUTH_ORDER - 1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTTokenAuthProvider jwtTokenAuthProvider;
    @Autowired
    private JWTRefreshTokenAuthProvider jwtRefreshTokenAuthProvider;

    /***
     * Config what resources need to be protected
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // no need CSRF by using JWT
            .csrf().disable()
            // no need session by using JWT
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            // authentication exception handling
            .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint()).and()
            // define route access rules
            .authorizeRequests()
            // allow static assets access by any user
            .antMatchers(
                HttpMethod.GET,
                "/",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
            ).permitAll()
            // allow auth api access by any user
            .antMatchers("/api/v1/auth/*").permitAll()
            // allow monitor access by only admin
            .antMatchers(actuatorEndpoints()).hasRole(Constant.MONITOR_ADMIN_ROLE)
            // allow main api access by only authenticated user
            .anyRequest().authenticated().and()
            // set logout action
            .logout().logoutUrl(RouteConfig.LOGOUT_URL)
            .logoutSuccessUrl(RouteConfig.LOGOUT_SUCCESS_URL).deleteCookies(Constant.AUTH_JWT_TOKEN_COOKIE);

        // add customized JWT authentication filter before spring security filter
        http.addFilterBefore(new AuthMainFilter(authenticationManager()), BasicAuthenticationFilter.class);

        // disable page caching
        //http.headers().cacheControl();
    }

    /**
     * Register authentication provider
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // register auth provider
        authenticationManagerBuilder
            .authenticationProvider(jwtTokenAuthProvider)
            .authenticationProvider(jwtRefreshTokenAuthProvider);
    }

    /**
     * Beans
     */
    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    private String[] actuatorEndpoints() {
        return new String[]{
            RouteConfig.AUTOCONFIG_ENDPOINT, RouteConfig.BEANS_ENDPOINT, RouteConfig.CONFIGPROPS_ENDPOINT,
            RouteConfig.ENV_ENDPOINT, RouteConfig.MAPPINGS_ENDPOINT, RouteConfig.METRICS_ENDPOINT,
            RouteConfig.SHUTDOWN_ENDPOINT};
    }
}