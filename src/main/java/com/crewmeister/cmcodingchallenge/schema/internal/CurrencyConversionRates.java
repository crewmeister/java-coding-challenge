package com.crewmeister.cmcodingchallenge.schema.internal;

import com.crewmeister.cmcodingchallenge.constants.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrencyConversionRates {
   private List<ConversionRate> conversionRateList;
}
