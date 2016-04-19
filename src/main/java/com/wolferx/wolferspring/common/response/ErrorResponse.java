package com.wolferx.wolferspring.common.response;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by chen on 4/17/16.
 */
public class ErrorResponse {

    private final Long timestamp;
    private final Integer status;
    private final String error;
    private final String message;
    private final String path;

    public ErrorResponse(final HttpStatus status, final String message, final HttpServletRequest request) {
        this.timestamp = new Date().getTime();
        this.status = status.value();
        this.error = status.toString();
        this.message = message;
        this.path = request.getRequestURL().toString();
    }
}
