package com.wolferx.wolferspring.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.wolferx.wolferspring.common.exception.InvalidRequestInputException;
import org.slf4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CommonUtils {

    public static Object parserJsonNode(final String key, final JsonNode sourceNode, final Class clazz, final Logger logger)
        throws InvalidRequestInputException {

        try {
            if (clazz.equals(String.class)) {
                return sourceNode.get(key).asText();
            } else if (clazz.equals(Long.class)){
                return sourceNode.get(key).asLong();
            } else if (clazz.equals(Integer.class)){
                return sourceNode.get(key).asInt();
            } else {
                throw new InvalidRequestInputException("Missing required input");
            }
        } catch (final NullPointerException nullPointerException) {
            logger.error("<In> parserJsonNode(): Missing required input", nullPointerException);
            throw new InvalidRequestInputException("Missing required input");
        }
    }

    public static void addCookie(final HttpServletResponse response, final String cookieName, final String cookieValue, final Integer cookieExpire) {
        final Cookie cookie = new Cookie(cookieName, cookieValue);
        //cookie.setSecure(true);
        cookie.setDomain("hightail.com");
        cookie.setPath("/");
        cookie.setMaxAge(cookieExpire);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static Optional<Cookie> getCookie(final HttpServletRequest request, final String cookieName) {
        final Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(cookieName)) {
                    return Optional.of(new Cookie(cookieName, cookies[i].getValue()));
                }
            }
        }
        return Optional.empty();
    }
}
