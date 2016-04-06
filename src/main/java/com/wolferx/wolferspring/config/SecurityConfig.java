package com.wolferx.wolferspring.config;

import com.wolferx.wolferspring.common.filter.AuthenticationFilter;
import com.wolferx.wolferspring.common.filter.ManagementEndpointAuthenticationFilter;
import com.wolferx.wolferspring.common.provider.BackendAdminUsernamePasswordAuthenticationProvider;
import com.wolferx.wolferspring.common.provider.TokenAuthProvider;
import com.wolferx.wolferspring.common.provider.UserPasswordAuthProvider;
import com.wolferx.wolferspring.external.ExternalServiceAuthenticator;
import com.wolferx.wolferspring.external.SomeExternalServiceAuthenticator;
import com.wolferx.wolferspring.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationProvider;
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
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${backend.admin.role}")
    private String backendAdminRole;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers(actuatorEndpoints()).hasRole(backendAdminRole)
                .anyRequest().permitAll()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint());
        // @formatter:on

        http.
            addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class).
            addFilterBefore(new ManagementEndpointAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManager) throws Exception {
        authenticationManager.
            authenticationProvider(domainUsernamePasswordAuthenticationProvider()).
            authenticationProvider(backendAdminUsernamePasswordAuthenticationProvider()).
            authenticationProvider(tokenAuthenticationProvider());
    }

    @Bean
    public TokenService tokenService() {
        return new TokenService();
    }

    @Bean
    public ExternalServiceAuthenticator someExternalServiceAuthenticator() {
        return new SomeExternalServiceAuthenticator();
    }

    @Bean
    public AuthenticationProvider domainUsernamePasswordAuthenticationProvider() {
        return new UserPasswordAuthProvider(tokenService(), someExternalServiceAuthenticator());
    }

    @Bean
    public AuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthProvider(tokenService());
    }

    @Bean
    public AuthenticationProvider backendAdminUsernamePasswordAuthenticationProvider() {
        return new BackendAdminUsernamePasswordAuthenticationProvider();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private String[] actuatorEndpoints() {

        return new String[]{
            RouteConfig.AUTOCONFIG_ENDPOINT, RouteConfig.BEANS_ENDPOINT,
            RouteConfig.CONFIGPROPS_ENDPOINT, RouteConfig.ENV_ENDPOINT,
            RouteConfig.MAPPINGS_ENDPOINT, RouteConfig.METRICS_ENDPOINT,
            RouteConfig.SHUTDOWN_ENDPOINT};
    }
}