package com.crewmeister.cmcodingchallenge.exchangerate.controller.advice;

import static com.crewmeister.cmcodingchallenge.exchangerate.model.Response.error;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.time.format.DateTimeParseException;

import com.crewmeister.cmcodingchallenge.exchangerate.exception.DataNotFoundException;
import com.crewmeister.cmcodingchallenge.exchangerate.model.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * ControllerAdvice to translate exceptions to HTTP STATUS codes
 *
 * @author jeevan
 */
@ControllerAdvice
public class ErrorAdvice extends ResponseEntityExceptionHandler {

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Response> handleDateTimeParseException(DateTimeParseException ex) {
        return new ResponseEntity<>(error(ex), BAD_REQUEST);
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Response> handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseEntity<>(error(ex), NOT_FOUND);
    }
}
