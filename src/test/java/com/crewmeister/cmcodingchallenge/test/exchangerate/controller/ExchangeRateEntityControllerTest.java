package com.crewmeister.cmcodingchallenge.test.exchangerate.controller;

import static java.time.LocalDate.of;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.crewmeister.cmcodingchallenge.exchangerate.controller.ExchangeRateController;
import com.crewmeister.cmcodingchallenge.exchangerate.model.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.exchangerate.service.ExchangeRateService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    public void getExchangeRates_returnsAllExchangeRates() throws Exception {
        given(exchangeRateService.getAllExchangeRates()).willReturn(List.of(
                new CurrencyExchangeRate(of(2020, 1, 1), "AUD", 1.1D),
                new CurrencyExchangeRate(of(2021, 1, 1), "AUD", 1.2D),
                new CurrencyExchangeRate(of(2022, 1, 1), "AUD", 1.3D)
        ));

        mockMvc.perform(get("/api/exchangerates")).
                andExpect(status().isOk()).

                andExpect(jsonPath("message").value("ok")).

                andExpect(jsonPath("data.[0].date").value("2020-01-01")).
                andExpect(jsonPath("data.[0].currencyCode").value("AUD")).
                andExpect(jsonPath("data.[0].exchangeRate").value("1.1")).

                andExpect(jsonPath("data.[1].date").value("2021-01-01")).
                andExpect(jsonPath("data.[1].currencyCode").value("AUD")).
                andExpect(jsonPath("data.[1].exchangeRate").value("1.2")).

                andExpect(jsonPath("data.[2].date").value("2022-01-01")).
                andExpect(jsonPath("data.[2].currencyCode").value("AUD")).
                andExpect(jsonPath("data.[2].exchangeRate").value("1.3"));
    }

    @Test
    public void testGetExchangeRatesForDay_returnsAllExchangeRatesForTheDay() throws Exception {
        given(exchangeRateService.getAllExchangeRates(of(2020, 1, 1))).willReturn(List.of(
                new CurrencyExchangeRate(of(2020, 1, 1), "AUD", 1.1D),
                new CurrencyExchangeRate(of(2020, 1, 1), "USD", 1.2D),
                new CurrencyExchangeRate(of(2020, 1, 1), "CAD", 1.3D)
        ));

        mockMvc.perform(get("/api/exchangerates/date/2020-01-01")).
                andExpect(status().isOk()).

                andExpect(jsonPath("message").value("ok")).

                andExpect(jsonPath("data.[0].date").value("2020-01-01")).
                andExpect(jsonPath("data.[0].currencyCode").value("AUD")).
                andExpect(jsonPath("data.[0].exchangeRate").value("1.1")).

                andExpect(jsonPath("data.[1].date").value("2020-01-01")).
                andExpect(jsonPath("data.[1].currencyCode").value("USD")).
                andExpect(jsonPath("data.[1].exchangeRate").value("1.2")).

                andExpect(jsonPath("data.[2].date").value("2020-01-01")).
                andExpect(jsonPath("data.[2].currencyCode").value("CAD")).
                andExpect(jsonPath("data.[2].exchangeRate").value("1.3"));
    }

    @Test
    public void testGetExchangeRatesForBadDayFormat_returnsHttpCodeBadRequest() throws Exception {
        given(exchangeRateService.getAllExchangeRates(null)).willReturn(List.of());

        mockMvc.perform(get("/api/exchangerates/date/abcdef")).
                andExpect(status().isBadRequest());
    }

    @Test
    public void testGetExchangeRateForDayOfCurrency_returnsExchangeRate() throws Exception {
        double rate = 1.5F;
        given(exchangeRateService.getExchangeRateByDateCurrencyCode(of(2020, 1, 1), "AUD"))
                .willReturn(rate);

        mockMvc.perform(get("/api/exchangerates/date/2020-01-01/currency/AUD")).
                andExpect(status().isOk()).

                andExpect(jsonPath("message").value("ok")).

                andExpect(jsonPath("data").value(Double.valueOf(rate).toString()));
    }

    @Test
    public void testGetExchangeRateForDayOfAmountOfCurrency_returnsAmountOnDay() throws Exception {
        double rate = 1.5F;
        given(exchangeRateService.getExchangeRateByDateCurrencyCode(of(2020, 1, 1), "AUD"))
                .willReturn(rate);

        mockMvc.perform(get("/api/exchangerates/date/2020-01-01/currency/AUD/amount/11")).
                andExpect(status().isOk()).

                andExpect(jsonPath("message").value("ok")).

                andExpect(jsonPath("data").value(Double.valueOf(rate * 11).toString()));
    }

    @Test
    public void testGetExchangeRateForDayOfAmountOfUnKnownCurrency_returnsHttpNotFound() throws Exception {
        given(exchangeRateService.getExchangeRateByDateCurrencyCode(of(2020, 1, 1), "CAD"))
                .willReturn(null);

        mockMvc.perform(get("/api/exchangerates/date/2020-01-01/currency/CAD/amount/11")).
                andExpect(status().isNotFound()).

                andExpect(jsonPath("message").value("error"));
    }
}
