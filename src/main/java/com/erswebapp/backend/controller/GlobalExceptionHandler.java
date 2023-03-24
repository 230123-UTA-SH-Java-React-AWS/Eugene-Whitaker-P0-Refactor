package com.erswebapp.backend.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.erswebapp.backend.exception.InvalidAccountException;
import com.erswebapp.backend.exception.InvalidActionException;
import com.erswebapp.backend.utilities.EmptyErrorResponseInfo;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @ExceptionHandler(value = InvalidAccountException.class)
    public final ResponseEntity<EmptyErrorResponseInfo> handleInvalidAccountException(InvalidAccountException e,
            HttpServletRequest request) {
        Date date = new Date();
        EmptyErrorResponseInfo error = new EmptyErrorResponseInfo(DATE_FORMAT.format(new Timestamp(date.getTime())),
                HttpStatus.BAD_REQUEST, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(value = InvalidActionException.class)
    public final ResponseEntity<EmptyErrorResponseInfo> handleInvalidActionException(InvalidActionException e,
            HttpServletRequest request) {
        Date date = new Date();
        EmptyErrorResponseInfo error = new EmptyErrorResponseInfo(DATE_FORMAT.format(new Timestamp(date.getTime())),
                HttpStatus.BAD_REQUEST, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(value = Exception.class)
    public final ResponseEntity<EmptyErrorResponseInfo> handleDefaultException(Exception e,
            HttpServletRequest request) {
        Date date = new Date();
        EmptyErrorResponseInfo error = new EmptyErrorResponseInfo(DATE_FORMAT.format(new Timestamp(date.getTime())),
                HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
