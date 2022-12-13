package com.crewmeister.cmcodingchallenge.exchangerate.controller;

import java.util.List;

import com.crewmeister.cmcodingchallenge.exchangerate.service.ExchangeRateService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController()
@RequestMapping("/api/currencies")
public class CurrencyController {

    private ExchangeRateService exchangeRateService;

    /**
     *
     * @param exchangeRateService
     */
    public CurrencyController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }
    /**
     *
     * @return
     */
    @GetMapping()
    public List<String> getCurrencies() {
        return exchangeRateService.getAllCurrencies();
    }
}
