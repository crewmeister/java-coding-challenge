package com.crewmeister.cmcodingchallenge.constants;

import java.util.Arrays;
import java.util.Optional;

public enum Currency {
    AUD("AUD"),
    BGN("BGN"),
    BRL("BRL"),
    CAD("CAD"),
    CHF("CHF"),
    CNY("CNY"),
    CYP("CYP"),
    CZK("CZK"),
    DKK("DKK"),
    EEK("EEK"),
    GBP("GBP"),
    GRD("GRD"),
    HKD("HKD"),
    HRK("HRK"),
    HUF("HUF"),
    IDR("IDR"),
    ILS("ILS"),
    INR("INR"),
    ISK("ISK"),
    JPY("JPY"),
    KRW("KRW"),
    LTL("LTL"),
    LVL("LVL"),
    MTL("MTL"),
    MXN("MXN"),
    MYR("MYR"),
    NOK("NOK"),
    NZD("NZD"),
    PHP("PHP"),
    PLN("PLN"),
    ROL("ROL"),
    RON("RON"),
    RUB("RUB"),
    SEK("SEK"),
    SGD("SGD"),
    SIT("SIT"),
    SKK("SKK"),
    THB("THB"),
    TRL("TRL"),
    TRY("TRY"),
    USD("USD"),
    ZAR("ZAR");
    private final String isoCode;

    Currency(String isoCode) {
        this.isoCode = isoCode;
    }

    public static Optional<Currency> getCurrency(String currencyCode) {
        return Arrays.stream(values())
                .filter(code -> code.isoCode.equalsIgnoreCase(currencyCode)).findFirst();
    }
}
