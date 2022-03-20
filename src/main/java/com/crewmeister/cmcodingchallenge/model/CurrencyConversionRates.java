package com.crewmeister.cmcodingchallenge.model;

import java.util.Map;

public class CurrencyConversionRates {

    private String date;
    private String base;
    private Map<String, Double> rates;

    public CurrencyConversionRates(String date, String base, Map<String, Double> rates) {
        this.date = date;
        this.base = base;
        this.rates = rates;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
