package com.crewmeister.cmcodingchallenge.exchangerate.controller;

import static com.crewmeister.cmcodingchallenge.exchangerate.model.Response.success;
import static java.lang.String.format;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ISO_DATE;

import com.crewmeister.cmcodingchallenge.exchangerate.exception.DataNotFoundException;
import com.crewmeister.cmcodingchallenge.exchangerate.model.Response;
import com.crewmeister.cmcodingchallenge.exchangerate.service.ExchangeRateService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling endpoints related to Exchange rates
 *
 * @author jeevan
 *
 */
@RestController()
@RequestMapping("/api/exchangerates")
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;

    /**
     * @param exchangeRateService
     */
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * @return
     */
    @GetMapping
    public Response getExchangeRates() {
        return success(exchangeRateService.getAllExchangeRates());
    }

    /**
     * @return
     */
    @GetMapping("/date/{date}")
    public Response getExchangeRatesForDate(@PathVariable("date") String date) {
        return success(exchangeRateService.getAllExchangeRates(parse(date, ISO_DATE)));
    }

    /**
     * @return
     */
    @GetMapping("/date/{date}/currency/{ccode}")
    public Response getExchangeRateByDateCurrencyCode(
            @PathVariable("date") String date,
            @PathVariable("ccode") String currencyCode) throws DataNotFoundException {

        Double rate = exchangeRateService.getExchangeRateByDateCurrencyCode(parse(date, ISO_DATE), currencyCode);
        if (rate == null) {
            throw new DataNotFoundException(format("Exchange Rate on %s for currency %s, not found", date, currencyCode));
        }

        return success(rate);
    }

    /**
     * @return
     */
    @GetMapping("/date/{date}/currency/{ccode}/amount/{amt}")
    public Response getAmountByDateCurrencyCode(
            @PathVariable("date") String date,
            @PathVariable("ccode") String currencyCode,
            @PathVariable("amt") Float amount) throws DataNotFoundException {

        Double rate = exchangeRateService.getExchangeRateByDateCurrencyCode(parse(date, ISO_DATE), currencyCode);
        if (rate == null) {
            throw new DataNotFoundException(format("Exchange Rate on %s for currency %s, not found", date, currencyCode));
        }

        return success(amount * rate);
    }
}
