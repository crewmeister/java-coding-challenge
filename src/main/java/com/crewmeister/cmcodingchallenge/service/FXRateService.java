package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.constants.Currency;
import com.crewmeister.cmcodingchallenge.schema.internal.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FXRateService {

    @Autowired
    private RestUtil restUtil;

    public List<CurrencyConversionRates>  getFXRates(String date, String currencyCode) {
        return null;
    }

}
