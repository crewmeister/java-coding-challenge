package com.crewmeister.cmcodingchallenge.exchangerate.service;

import java.time.LocalDate;
import java.util.List;

import com.crewmeister.cmcodingchallenge.exchangerate.model.CurrencyExchangeRate;

/**
 * Interface defining all the Exchange rate apis
 *
 * @author jeevan
 */
public interface ExchangeRateService {

    /**
     * Returns the list of all known currencies
     *
     * @return
     */
    List<String> getAllCurrencies();

    /**
     * Returns all the Exchange rates on all days
     *
     * @return
     */
    List<CurrencyExchangeRate> getAllExchangeRates();

    /**
     * Returns all the Exchange rates on a given day
     *
     * @param date
     * @return
     */
    List<CurrencyExchangeRate> getAllExchangeRates(LocalDate date);

    /**
     * Returns the Exchange rate on a given day - returns null if the exchange rate is not found
     *
     * @param date
     * @param currencyCode
     * @return
     */
    Double getExchangeRateByDateCurrencyCode(LocalDate date, String currencyCode);
}
