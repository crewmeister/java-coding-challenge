package com.crewmeister.cmcodingchallenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RateNotAvailableException extends Exception {
    private String date;
}
