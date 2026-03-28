package com.crewmeister.cmcodingchallenge.currency;

import javax.validation.Valid;
import java.util.List;

public class CurrencyWrapper {

    @Valid
    private List<Currency> currencies;

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }
}

