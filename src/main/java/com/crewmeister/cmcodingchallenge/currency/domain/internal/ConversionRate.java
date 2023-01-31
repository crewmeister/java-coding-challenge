package com.crewmeister.cmcodingchallenge.currency.domain.internal;

import com.crewmeister.cmcodingchallenge.currency.constants.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConversionRate {
    private String conversionRate;
    private String date;
    private Currency currency;
}
