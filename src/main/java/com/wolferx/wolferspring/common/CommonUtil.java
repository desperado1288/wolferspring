package com.wolferx.wolferspring.common;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    public static final Boolean isNullEmpty(String string) {
        return (string == null || string.equals(""));
    }
}
