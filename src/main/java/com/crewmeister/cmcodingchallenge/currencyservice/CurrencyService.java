package com.crewmeister.cmcodingchallenge.currencyservice;

import com.crewmeister.cmcodingchallenge.currency.Currency;

import java.util.List;
import java.util.Map;


public interface CurrencyService {

    public List<Currency> getListOfAvailableCurrencies();

    public List<Currency> addCurrency(List<Currency> currency);

    public Map<String, Map<String,String>> getFXRates(String date,String currency);

    public double getFXAmount(String date, String currency, double amount);
}
