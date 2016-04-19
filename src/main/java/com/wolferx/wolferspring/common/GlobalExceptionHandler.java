package com.wolferx.wolferspring.common;


import com.wolferx.wolferspring.common.exception.AuthServiceException;
import com.wolferx.wolferspring.common.exception.DuplicateItemException;
import com.wolferx.wolferspring.common.exception.InvalidInputException;
import com.wolferx.wolferspring.common.exception.ItemNotFoundException;
import com.wolferx.wolferspring.common.exception.LogicalErrorException;
import com.wolferx.wolferspring.common.exception.StorageServiceException;
import com.wolferx.wolferspring.common.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<Object> authServiceException(final HttpServletRequest request, final AuthServiceException exception) {
        logger.error("<Exception> Request: " + request.getRequestURL() + " AuthServiceException: " + exception);
        return CommonUtils.genErrorResponse(HttpStatus.UNAUTHORIZED, "Unable to authenticate User with provided credentials", request);
    }

    @ExceptionHandler(DuplicateItemException.class)
    public ResponseEntity<Object> duplicateItemExceptionHandler(final HttpServletRequest request, final DuplicateItemException exception) {
        logger.error("<Exception> Request: " + request.getRequestURL() + " DuplicateItemException: " + exception);
        return CommonUtils.genErrorResponse(HttpStatus.CONFLICT, "Duplicate Item Already Exist", request);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Object> invalidInputExceptionHandler(final HttpServletRequest request, final InvalidInputException exception) {
        logger.error("<Exception> Request: " + request.getRequestURL() + " InvalidInputException: " + exception);
        return CommonUtils.genErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Input Arguments", request);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Object> itemNotFoundExceptionHandler(final HttpServletRequest request, final ItemNotFoundException exception) {
        logger.error("<Exception> Request: " + request.getRequestURL() + " ItemNotFoundException: " + exception);
        return CommonUtils.genErrorResponse(HttpStatus.NOT_FOUND, "Item Not Found", request);
    }

    @ExceptionHandler(LogicalErrorException.class)
    public ResponseEntity<Object> logicalErrorExceptionHandler(final HttpServletRequest request, final LogicalErrorException exception) {
        logger.error("<Exception> Request: " + request.getRequestURL() + " LogicalErrorException: " + exception);
        return CommonUtils.genErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Logical Error", request);
    }

    @ExceptionHandler(StorageServiceException.class)
    public ResponseEntity<Object> storageServiceExceptionHandler(final HttpServletRequest request, final StorageServiceException exception) {
        logger.error("<Exception> Request: " + request.getRequestURL() + " StorageServiceException: " + exception);
        return CommonUtils.genErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Storage Service Error", request);
    }

    // generic exception handler
    /*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultErrorHandler(final HttpServletRequest request, final Exception exception) {
        logger.error("Request: " + request.getRequestURL() + " DefaultException " + exception);
        return CommonUtils.genErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled Exception", request);
    }
    */

}

