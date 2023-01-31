package com.crewmeister.cmcodingchallenge.currency;

import com.crewmeister.cmcodingchallenge.constants.Currency;
import com.crewmeister.cmcodingchallenge.exception.CurrencyNotSupportedException;
import com.crewmeister.cmcodingchallenge.schema.internal.ConversionRate;
import com.crewmeister.cmcodingchallenge.schema.internal.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.schema.internal.CurrencyResponse;
import com.crewmeister.cmcodingchallenge.schema.internal.ExchangedAmount;
import com.crewmeister.cmcodingchallenge.service.FXRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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
    public ResponseEntity<ExchangedAmount> getExchangeValue(
            @RequestParam() String currencyCode,
            @RequestParam() String date,
            @RequestParam() String amount
    ) {
        Optional<ConversionRate> currencyConversionRate = null;
        try {
            currencyConversionRate = fxRateService.getFXRates(currencyCode, date).getConversionRateList().stream().findFirst();
        } catch (CurrencyNotSupportedException e) {
            handleCurrencyNotSupportedException();
        }
        var exchangedAmount = new ExchangedAmount();

        if (currencyConversionRate.isPresent()) {
            exchangedAmount.setAmount(currencyConversionRate.get().getConversionRate() * Double.parseDouble(amount));
            exchangedAmount.setConversionRate(currencyConversionRate.get());
        }
        return new ResponseEntity<>(exchangedAmount, HttpStatus.OK);
    }

    private void handleCurrencyNotSupportedException() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency Not Supported");
    }
}
