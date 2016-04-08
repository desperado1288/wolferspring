package com.wolferx.wolferspring.config;

import com.wolferx.wolferspring.common.filter.AuthMainFilter;
import com.wolferx.wolferspring.common.filter.AuthMonitorFilter;
import com.wolferx.wolferspring.common.security.external.ExternalServiceAuthenticator;
import com.wolferx.wolferspring.common.security.external.SomeExternalServiceAuthenticator;
import com.wolferx.wolferspring.common.security.provider.BackendAdminUsernamePasswordAuthenticationProvider;
import com.wolferx.wolferspring.common.security.provider.JWTAuthProvider;
import com.wolferx.wolferspring.common.security.provider.PasswordAuthProvider;
import com.wolferx.wolferspring.service.AuthService;
import com.wolferx.wolferspring.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableScheduling
@Order(ManagementServerProperties.BASIC_AUTH_ORDER - 1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${backend.admin.role}")
    private String backendAdminRole;
    @Autowired
    private AuthService authService;
    @Autowired
    private TokenService tokenService;


    /***
     * Config what resources need to be protected
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(actuatorEndpoints()).hasRole(backendAdminRole)
            .anyRequest().permitAll()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
            .addFilterBefore(new AuthMainFilter(authenticationManager()), BasicAuthenticationFilter.class)
            .addFilterBefore(new AuthMonitorFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    /***
     * Register authentication provider
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // quick auth access
        authenticationManagerBuilder
            .inMemoryAuthentication().withUser("dave")
            .password("secret").roles("USER");

        // custom auth provider
        authenticationManagerBuilder
            .authenticationProvider(passwordAuthProvider())
            .authenticationProvider(backendAdminUsernamePasswordAuthenticationProvider())
            .authenticationProvider(jwtAuthProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationProvider passwordAuthProvider() {
        return new PasswordAuthProvider(this.authService);
    }

    @Bean
    public AuthenticationProvider jwtAuthProvider() {
        return new JWTAuthProvider(this.tokenService);
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider backendAdminUsernamePasswordAuthenticationProvider() {
        return new BackendAdminUsernamePasswordAuthenticationProvider();
    }

    @Bean
    public ExternalServiceAuthenticator someExternalServiceAuthenticator() {
        return new SomeExternalServiceAuthenticator();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    private String[] actuatorEndpoints() {

        return new String[]{
            RouteConfig.AUTOCONFIG_ENDPOINT, RouteConfig.BEANS_ENDPOINT,
            RouteConfig.CONFIGPROPS_ENDPOINT, RouteConfig.ENV_ENDPOINT,
            RouteConfig.MAPPINGS_ENDPOINT, RouteConfig.METRICS_ENDPOINT,
            RouteConfig.SHUTDOWN_ENDPOINT};
    }
}