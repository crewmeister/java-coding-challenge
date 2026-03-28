package com.crewmeister.cmcodingchallenge.currencydto;

import com.crewmeister.cmcodingchallenge.currency.CurrencyConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public class FXRequestDto {

    @NotNull(message = "Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Currency is required")
    private CurrencyConstants currency;

    @NotNull
    @Positive(message = "Amount must be a positive number")
    private Double amount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CurrencyConstants getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyConstants currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
