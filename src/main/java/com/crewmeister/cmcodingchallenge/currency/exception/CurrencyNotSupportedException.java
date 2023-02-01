package com.crewmeister.cmcodingchallenge.currency.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrencyNotSupportedException extends Exception {
    private String isoCode;
}
