package com.wolferx.wolferspring.config;

public class RouteConfig {

    private static final String API_PATH = "/api/v1";

    public static final String USER_URL = API_PATH + "/user";
    public static final String POST_URL = API_PATH + "/post";
    public static final String AUTHENTICATE_URL = API_PATH + "/authenticate";
    public static final String STUFF_URL = API_PATH + "/stuff";


    // Spring Boot Actuator services
    public static final String AUTOCONFIG_ENDPOINT = "/autoconfig";
    public static final String BEANS_ENDPOINT = "/beans";
    public static final String CONFIGPROPS_ENDPOINT = "/configprops";
    public static final String ENV_ENDPOINT = "/env";
    public static final String MAPPINGS_ENDPOINT = "/mappings";
    public static final String METRICS_ENDPOINT = "/metrics";
    public static final String SHUTDOWN_ENDPOINT = "/shutdown";
}
