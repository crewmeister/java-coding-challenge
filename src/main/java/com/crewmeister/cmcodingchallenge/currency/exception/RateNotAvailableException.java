package com.crewmeister.cmcodingchallenge.currency.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RateNotAvailableException extends Exception {
    private String date;
}
