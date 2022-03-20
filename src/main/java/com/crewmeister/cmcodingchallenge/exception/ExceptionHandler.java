package com.crewmeister.cmcodingchallenge.exception;

import com.crewmeister.cmcodingchallenge.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler()
    public final ResponseEntity<ErrorMessage> inputFormatException(Exception ex) {
        ErrorMessage m = new ErrorMessage(ex.getMessage(), "2020-02-01 is correct!");
        return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
    }
}
