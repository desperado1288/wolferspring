package com.wolferx.wolferspring.common.constant;

public class Constant {

    /**
     * System
     */
    public final static String  MONITOR_ADMIN_ROLE = "ADMIN";

    /**
     * CORS
     */
    public final static String  ACCESS_CONTROL_ALLOW_ORIGIN = "*";
    public final static String  ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, PUT, DELETE, OPTIONS";
    public final static String  ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
    public final static String  ACCESS_CONTROL_MAX_AGE = "3600"; // 60 * 60 = 1 HOUR
    public final static String  ACCESS_CONTROL_ALLOW_HEADERS = "Origin, X-Requested-With, X-Prototype-Version, Content-Type, Cache-Control, Pragma, Accept";

    /**
     * Response
     */
    public final static String  RESPONSE_ACTION_SUCCESS = "ACTION HAS BEEN EXECUTED SUCCESSFULLY";

    /**
     * User
     */
    public final static Integer  USER_NOT_VERIFIED = 0;
    public final static Integer  USER_VERIFIED = 1;
    public final static Integer  USER_STATUS_INACTIVE = 0;
    public final static Integer  USER_STATUS_ACTIVE = 1;

    /**
     * Auth
     */
    public final static String  AUTH_USERNAME_HEADER            = "X-Auth-Username";
    public final static String  AUTH_PASSWORD_HEADER            = "X-Auth-Password";
    public final static String  AUTH_JWT_SECRET                 = "c3VwZXJfdG9rZW5feA==";
    public final static Integer AUTH_JWT_SALT_LENGTH            = 8;
    public final static String  AUTH_JWT_TOKEN_HEADER           = "Authorization";
    public final static String  AUTH_JWT_REFRESH_TOKEN_HEADER    = "Authorization-Refresh";
    public final static String  AUTH_JWT_TOKEN_COOKIE           = "wolferx_token";
    public final static String  AUTH_JWT_REFRESH_TOKEN_COOKIE   = "wolferx_refresh";
    public final static Integer AUTH_JWT_TOKEN_EXPIRE           = 60 * 15; // 15 Minutes
    public final static Integer AUTH_JWT_REFRESH_TOKEN_EXPIRE   = 60 * 60 * 24 * 30; // 1 Month

    /**
     * Post
     */
    public final static Integer POST_STATUS_INACTIVE = 0;
    public final static Integer POST_STATUS_ACTIVE = 1;
    public static final Integer POST_TYPE_TEXT = 1;
    public static final Integer POST_TYPE_MUSIC = 2;

    /**
     * Comment
     */
    public final static Integer COMMENT_STATUS_INACTIVE = 0;
    public final static Integer COMMENT_STATUS_ACTIVE = 1;
}
