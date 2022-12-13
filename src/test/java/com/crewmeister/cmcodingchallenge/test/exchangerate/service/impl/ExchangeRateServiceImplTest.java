package com.crewmeister.cmcodingchallenge.test.exchangerate.service.impl;

import static java.lang.String.format;
import static java.time.LocalDate.of;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.join;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.List;

import com.crewmeister.cmcodingchallenge.exchangerate.model.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.exchangerate.persistence.entity.CurrencyExchangeRateEntity;
import com.crewmeister.cmcodingchallenge.exchangerate.persistence.repository.CurrencyExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.exchangerate.service.ExchangeRateService;
import com.crewmeister.cmcodingchallenge.exchangerate.service.impl.ExchangeRateServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceImplTest {

    @Mock
    private CurrencyExchangeRateRepository repository;

    private ExchangeRateService testService;

    @BeforeEach
    void setUp() {
        this.testService = new ExchangeRateServiceImpl(repository);
    }

    @Test
    public void testGetAllCurrencies_returnsAllCurrencies() {
        List<String> mockList = List.of("AUD", "USD", "PND");

        given(this.repository.findDistinctCurrencyCode()).willReturn(mockList);

        List<String> currencies = this.testService.getAllCurrencies();

        assertNotNull(currencies, "Currencies returned by service is null");
        assertEquals(mockList.size(), currencies.size());
        assertTrue(currencies.containsAll(mockList), format("Expected %s, found %s", join(mockList), join(currencies)));
    }

    @Test
    public void testGetAllExchangeRates_returnsAllExchangeRates() {
        List<CurrencyExchangeRate> mockList = List.of(
                new CurrencyExchangeRate(of(2020, 1, 1), "AUD", 1.1D),
                new CurrencyExchangeRate(of(2021, 1, 1), "AUD", 1.2D),
                new CurrencyExchangeRate(of(2022, 1, 1), "AUD", 1.3D)
        );

        given(this.repository.findAll()).willReturn(mockList.stream()
                .map(e -> new CurrencyExchangeRateEntity(e.getDate(), e.getCurrencyCode(), e.getExchangeRate()))
                .collect(toList()));

        List<CurrencyExchangeRate> allExchangeRates = this.testService.getAllExchangeRates();

        assertNotNull(allExchangeRates, "ExchangeRates returned by service is null");
        assertEquals(mockList.size(), allExchangeRates.size());
        assertTrue(allExchangeRates.containsAll(mockList), format("Expected %s, found %s", join(mockList), join(allExchangeRates)));
    }

    @Test
    public void testGetExchangeRatesForDay_returnsAllExchangeRatesForDay() {
        LocalDate date              = of(2020, 1, 1);
        List<CurrencyExchangeRate> mockList = List.of(
                new CurrencyExchangeRate(date, "AUD", 1.1D),
                new CurrencyExchangeRate(date, "USD", 1.2D),
                new CurrencyExchangeRate(date, "CAD", 1.3D)
        );

        given(this.repository.findByDate(date)).willReturn(mockList.stream()
                .map(e -> new CurrencyExchangeRateEntity(e.getDate(), e.getCurrencyCode(), e.getExchangeRate()))
                .collect(toList()));

        List<CurrencyExchangeRate> allExchangeRates = this.testService.getAllExchangeRates(date);

        assertNotNull(allExchangeRates, "ExchangeRates returned by service is null");
        assertEquals(mockList.size(), allExchangeRates.size());
        assertTrue(allExchangeRates.containsAll(mockList), format("Expected %s, found %s", join(mockList), join(allExchangeRates)));
    }

    @Test
    public void testGetExchangeRateByDateCurrencyCode_returnsExchangeRate() {
        LocalDate date = of(2020, 1, 1);
        String currencyCode = "AUD";
        double expectedExchangeRate = 1.1D;

        given(this.repository.findByDateAndCurrencyCode(date, currencyCode)).willReturn(
                new CurrencyExchangeRateEntity(date, currencyCode, expectedExchangeRate));

        Double actual = this.testService.getExchangeRateByDateCurrencyCode(date, currencyCode);

        assertNotNull(actual, "ExchangeRate returned by service is null");
        assertEquals(expectedExchangeRate, actual);
    }

    @Test
    public void testGetExchangeRateByNonExistentDateCurrencyCode_returnsNull() {
        LocalDate date = of(2020, 1, 1);
        String currencyCode = "AUD";

        given(this.repository.findByDateAndCurrencyCode(date, currencyCode)).willReturn(null);

        Double actual = this.testService.getExchangeRateByDateCurrencyCode(date, currencyCode);

        assertNull(actual, "ExchangeRate returned by service is not null");
    }
}
