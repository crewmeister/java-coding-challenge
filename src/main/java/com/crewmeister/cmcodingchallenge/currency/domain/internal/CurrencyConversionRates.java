package com.crewmeister.cmcodingchallenge.currency.domain.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionRates {
    private List<ConversionRate> conversionRateList;
}
