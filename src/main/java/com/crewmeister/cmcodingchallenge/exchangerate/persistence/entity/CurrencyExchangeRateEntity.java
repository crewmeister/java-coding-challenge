package com.crewmeister.cmcodingchallenge.exchangerate.persistence.entity;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_DATE;

import javax.persistence.*;
import java.time.LocalDate;

import lombok.Data;

/**
 * Entity holding values for the Currency Exchange Rate per date
 *
 * @author jeevan
 */
@Entity
@Table(name = "CURRENCY_EXCHANGE_RATE", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueCurrencyCodeAndDate", columnNames = {
                "currencyCode", "date"
        })
})
@Data
public class CurrencyExchangeRateEntity {
    @Id
    private final String id;

    @Column(nullable = false)
    private final LocalDate date;

    @Column(nullable = false)
    private final Double exchangeRate;

    @Column(nullable = false)
    private final String currencyCode;

    public CurrencyExchangeRateEntity() {
        this(null, null, null);
    }

    /**
     * @param date
     * @param exchangeRate
     * @param currencyCode
     */
    public CurrencyExchangeRateEntity(LocalDate date, String currencyCode, Double exchangeRate) {
        this.date           = date;
        this.currencyCode   = currencyCode;
        this.exchangeRate   = exchangeRate;

        if (currencyCode == null || date == null) {
            this.id = null;
            return;
        }

        this.id = format("%s_%s", currencyCode, date.format(ISO_DATE));
    }
}
