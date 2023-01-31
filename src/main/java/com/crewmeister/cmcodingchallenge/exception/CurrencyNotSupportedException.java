package com.crewmeister.cmcodingchallenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
public class CurrencyNotSupportedException extends Exception {
    private String isoCode;
}
