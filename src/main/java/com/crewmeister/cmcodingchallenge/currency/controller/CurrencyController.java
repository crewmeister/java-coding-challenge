package com.crewmeister.cmcodingchallenge.currency.controller;

import com.crewmeister.cmcodingchallenge.currency.constants.Currency;
import com.crewmeister.cmcodingchallenge.currency.exception.CurrencyNotSupportedException;
import com.crewmeister.cmcodingchallenge.currency.exception.RateNotAvailableException;
import com.crewmeister.cmcodingchallenge.currency.domain.internal.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.currency.domain.internal.CurrencyResponse;
import com.crewmeister.cmcodingchallenge.currency.domain.internal.ExchangedAmount;
import com.crewmeister.cmcodingchallenge.currency.service.FXRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController()
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    private FXRateService fxRateService;

    @GetMapping("/currencies")
    public ResponseEntity<CurrencyResponse> getCurrencies() {
        return new ResponseEntity<>(new CurrencyResponse(Currency.values()), HttpStatus.OK);
    }

    @GetMapping("/exchange-rates")
    public ResponseEntity<CurrencyConversionRates> getExchangeRates(
            @RequestParam() String currencyCode,
            @RequestParam(required = false) String date
    ) {
        CurrencyConversionRates rates = null;
        try {
            rates = fxRateService.getFXRates(currencyCode, date);
        } catch (CurrencyNotSupportedException e) {
            handleCurrencyNotSupportedException();
        }
        return new ResponseEntity<>(rates, HttpStatus.OK);
    }

    @GetMapping("/exchange-currency")
    public ResponseEntity<ExchangedAmount> getCurrencyExchange(
            @RequestParam() String currencyCode,
            @RequestParam() String amount,
            @RequestParam(required = false) String date

    ) {
        ExchangedAmount exchangedAmount = null;
        try {
            exchangedAmount = fxRateService.getFXValue(currencyCode, amount, date);
        } catch (CurrencyNotSupportedException e) {
            handleCurrencyNotSupportedException();
        } catch (RateNotAvailableException e) {
            handleRateNotAvailableException();
        }
        return new ResponseEntity<>(exchangedAmount, HttpStatus.OK);
    }

    private void handleCurrencyNotSupportedException() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency Not Supported");
    }

    private void handleRateNotAvailableException() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rate Not Available");
    }
}
