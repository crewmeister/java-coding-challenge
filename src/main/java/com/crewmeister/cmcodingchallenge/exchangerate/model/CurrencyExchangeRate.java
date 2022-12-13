package com.crewmeister.cmcodingchallenge.exchangerate.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class providing abstraction (filter) for the underlying entity
 * to be used in rest calls
 *
 * @author jeevan
 */
@Data
@AllArgsConstructor
public class CurrencyExchangeRate {
    private final LocalDate date;

    private final String currencyCode;

    private final Double exchangeRate;
}
