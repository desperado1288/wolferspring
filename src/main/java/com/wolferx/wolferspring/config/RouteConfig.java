package com.wolferx.wolferspring.config;

public class RouteConfig {

    // API
    private static final String API_PATH = "/api/v1";

    public static final String USER_URL = API_PATH + "/user";
    public static final String POST_URL = API_PATH + "/post";
    public static final String AUTH_URL = API_PATH + "/auth";

    // Logout
    public static final String LOGOUT_URL = "/logout";
    public static final String LOGOUT_SUCCESS_URL = "/";

    // Spring Boot Actuator services
    public static final String AUTOCONFIG_ENDPOINT = "/autoconfig";
    public static final String BEANS_ENDPOINT = "/beans";
    public static final String CONFIGPROPS_ENDPOINT = "/configprops";
    public static final String ENV_ENDPOINT = "/env";
    public static final String MAPPINGS_ENDPOINT = "/mappings";
    public static final String METRICS_ENDPOINT = "/metrics";
    public static final String SHUTDOWN_ENDPOINT = "/shutdown";
}
