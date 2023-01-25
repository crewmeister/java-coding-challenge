package com.crewmeister.cmcodingchallenge.currency;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyConversionRates {
    private double conversionRate;

    public CurrencyConversionRates(double conversionRate) {
        this.conversionRate = conversionRate;
    }
}
