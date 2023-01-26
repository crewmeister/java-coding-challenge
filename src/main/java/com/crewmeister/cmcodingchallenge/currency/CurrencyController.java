package com.crewmeister.cmcodingchallenge.currency;

import com.crewmeister.cmcodingchallenge.constants.Currency;
import com.crewmeister.cmcodingchallenge.schema.external.GenericData;
import com.crewmeister.cmcodingchallenge.schema.internal.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.schema.internal.CurrencyResponse;
import com.crewmeister.cmcodingchallenge.util.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/api")
public class CurrencyController {
    private static final Logger log = LoggerFactory.getLogger(CurrencyController.class);


    String url = "https://api.statistiken.bundesbank.de/rest/data/BBEX3/D.PLN.EUR.BB.AC.000?detail=dataonly&lastNObservations=10";

    @GetMapping("/currencies")
    public ResponseEntity<CurrencyResponse> getCurrencies() {
        return new ResponseEntity(new CurrencyResponse(Currency.values()), HttpStatus.OK);
    }

    @GetMapping("/exchangerates")
    public ResponseEntity<ArrayList<CurrencyConversionRates>> getExchangeRates() {
        //ArrayList<CurrencyConversionRates> currencyConversionRates = new ArrayList<CurrencyConversionRates>();
        //currencyConversionRates.add(new CurrencyConversionRates(3.5));

        return new ResponseEntity<ArrayList<CurrencyConversionRates>>(HttpStatus.OK);
    }

}
