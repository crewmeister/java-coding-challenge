package com.crewmeister.cmcodingchallenge.currencycontroller;

import com.crewmeister.cmcodingchallenge.currency.Currency;
import com.crewmeister.cmcodingchallenge.currency.CurrencyConstants;
import com.crewmeister.cmcodingchallenge.currency.CurrencyWrapper;
import com.crewmeister.cmcodingchallenge.currencydto.FXRequestDto;
import com.crewmeister.cmcodingchallenge.currencyservice.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/cmfxapi")
public class CurrencyController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
    @Autowired
    CurrencyService currencyService;

    @PostMapping("/addCurrencies")
    public List<Currency> addCurrency(@RequestBody @Valid CurrencyWrapper currency) {
        logger.info("Adding the list of Currency");
        if (currency.getCurrencies() == null || currency.getCurrencies().isEmpty()) {
            throw new IllegalArgumentException("Currency list must not be null or empty");
        }
        return currencyService.addCurrency(currency.getCurrencies());
    }

    @GetMapping("/getAllAvailableCurrencies")
    public ResponseEntity<List<String>> getListOfAvailableCurrencies() {
        logger.info("Fetching the list of Currencies");
        List<Currency> currencies = currencyService.getListOfAvailableCurrencies();
        List<String> getCurrencyValues = new ArrayList<>();
        if(!currencies.isEmpty())
        getCurrencyValues =currencies.stream().map(x -> x.getCurrencyName())
                .collect(Collectors.toList());
        else { //if db doesnt have data , we have retrieved data from enum
            for(CurrencyConstants ccy: CurrencyConstants.values())
                getCurrencyValues.add(ccy.name());
        }
        return new ResponseEntity<List<String>>(getCurrencyValues, HttpStatus.OK);
    }

    @GetMapping("/getAllFXRates")
    public ResponseEntity<Map<String, Map<String,String>>> getAllFXRates(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam(required = false) String currency) {
        logger.info("Fetching the fx rates of Currencies");
        Map<String, Map<String,String>> fxRates = currencyService.getFXRates(date != null ? date.toString() : null,currency);
        return new ResponseEntity<Map<String, Map<String,String>>>(fxRates, HttpStatus.OK);
    }

    @GetMapping("/getFXAmount")
    public ResponseEntity<Double> getFXAmount(@Valid FXRequestDto request) {
        logger.info("Received FX request: {}", request);
        double fxAmount = currencyService.getFXAmount(request.getDate().toString(),request.getCurrency().name(), request.getAmount());
        return new ResponseEntity<Double>(fxAmount, HttpStatus.OK);
    }
}
