package com.crewmeister.cmcodingchallenge.schema.internal;

import com.crewmeister.cmcodingchallenge.constants.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CurrencyResponse {
    public CurrencyResponse() {
    }
    private Currency[] currencies;
}
