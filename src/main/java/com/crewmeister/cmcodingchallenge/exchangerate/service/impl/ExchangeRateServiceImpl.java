package com.crewmeister.cmcodingchallenge.exchangerate.service.impl;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.time.LocalDate;
import java.util.List;

import com.crewmeister.cmcodingchallenge.exchangerate.model.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.exchangerate.persistence.entity.CurrencyExchangeRateEntity;
import com.crewmeister.cmcodingchallenge.exchangerate.persistence.repository.CurrencyExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.exchangerate.service.ExchangeRateService;

import org.springframework.stereotype.Service;

/**
 * Default implementation of the ExchangeRateService
 *
 * @author jeevan
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final CurrencyExchangeRateRepository repository;

    /**
     * Constructor for dependency injection
     *
     * @param repository
     */
    public ExchangeRateServiceImpl(CurrencyExchangeRateRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns the list of all known currencies
     *
     * @return
     */
    @Override
    public List<String> getAllCurrencies() {
        return this.repository.findDistinctCurrencyCode();
    }

    /**
     * Returns all the Exchange rates on all days
     *
     * @return
     */
    @Override
    public List<CurrencyExchangeRate> getAllExchangeRates() {
        return transform(this.repository.findAll());
    }

    /**
     * Returns all the Exchange rates on a given day
     *
     * @param date
     * @return
     */
    @Override
    public List<CurrencyExchangeRate> getAllExchangeRates(LocalDate date) {
        return transform(this.repository.findByDate(date));
    }

    /**
     * Returns the Exchange rate on a given day - returns null if the exchange rate is not found
     *
     * @param date
     * @param currencyCode
     * @return
     */
    @Override
    public Double getExchangeRateByDateCurrencyCode(LocalDate date, String currencyCode) {
        CurrencyExchangeRateEntity rate = this.repository.findByDateAndCurrencyCode(date, currencyCode);
        if (isNull(rate)) {
            return null;
        }

        return rate.getExchangeRate();
    }

    /**
     *
     * @param all
     * @return
     */
    private static List<CurrencyExchangeRate> transform(List<CurrencyExchangeRateEntity> all) {
        if (isEmpty(all)) {
            return emptyList();
        }

        return all.stream().
                map(e -> new CurrencyExchangeRate(e.getDate(), e.getCurrencyCode(), e.getExchangeRate())).
                collect(toList());
    }
}
