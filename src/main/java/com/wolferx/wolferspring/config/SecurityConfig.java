package com.wolferx.wolferspring.config;

import com.wolferx.wolferspring.common.filter.AuthMainFilter;
import com.wolferx.wolferspring.common.security.JWTAuthProvider;
import com.wolferx.wolferspring.common.security.PasswordAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Value("${monitor.admin.role}")
    private String monitorAdminRole;
    @Autowired
    private PasswordAuthProvider passwordAuthProvider;
    @Autowired
    private JWTAuthProvider jwtAuthProvider;

    /***
     * Config what resources need to be protected
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(actuatorEndpoints()).hasRole(monitorAdminRole)
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
            .logout()
            .logoutUrl(RouteConfig.LOGOUT_URL)
            .logoutSuccessUrl(RouteConfig.LOGOUT_SUCCESS_URL)
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedEntryPoint())
            .and()
            .addFilterBefore(new AuthMainFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    /***
     * Register authentication provider
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // custom auth provider
        authenticationManagerBuilder
            .authenticationProvider(passwordAuthProvider)
            .authenticationProvider(jwtAuthProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

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