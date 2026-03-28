package com.crewmeister.cmcodingchallenge.currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Currency {

    @Id
    @GeneratedValue
    private Long currencyId;

    @NotBlank(message = "Currency name must not be blank")
    private String currencyName;

    public Currency() {
    }

    public Currency(Long currencyId, String currencyName) {
        this.currencyId = currencyId;
        this.currencyName = currencyName;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
