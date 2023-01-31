package com.crewmeister.cmcodingchallenge.schema.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangedAmount {
    private String amount;
    private ConversionRate conversionRate;
}