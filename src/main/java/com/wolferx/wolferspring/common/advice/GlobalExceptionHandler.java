package com.wolferx.wolferspring.common.advice;


import com.wolferx.wolferspring.common.ErrorMessage;
import com.wolferx.wolferspring.common.exception.BaseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class, BaseException.class})
    @ResponseBody
    public ErrorMessage badRequestHandler(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        return new ErrorMessage("Bad Request 123");
    }
}
