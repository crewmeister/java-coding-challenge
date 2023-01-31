package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.constants.Currency;
import com.crewmeister.cmcodingchallenge.exception.CurrencyNotSupportedException;
import com.crewmeister.cmcodingchallenge.exception.RateNotAvailableException;
import com.crewmeister.cmcodingchallenge.schema.external.GenericData;
import com.crewmeister.cmcodingchallenge.schema.internal.ConversionRate;
import com.crewmeister.cmcodingchallenge.schema.internal.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.schema.internal.ExchangedAmount;
import com.crewmeister.cmcodingchallenge.util.RestUtil;
import com.crewmeister.cmcodingchallenge.util.SchemaTransformer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

import static org.apache.tomcat.util.http.FastHttpDateFormat.getCurrentDate;

@Slf4j
@Service
public class FXRateService {

    @Value("${external.fx.scheme}")
    private String fxScheme;

    @Value("${external.fx.host}")
    private String fxHost;

    @Autowired
    private RestUtil restUtil;

    private static final String TIME_SERIES = "BBEX3/D.XXX.EUR.BB.AC.000";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

    public CurrencyConversionRates getFXRates(String currencyCode, String date) throws CurrencyNotSupportedException {
        validateSupportedCurrency(currencyCode);
        UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance()
                .scheme(fxScheme).host(fxHost)
                .path(TIME_SERIES.replace("XXX", currencyCode))
                .queryParam("detail", "dataonly");

        if (date != null) {
            uriComponents.queryParam("startPeriod", date)
                    .queryParam("endPeriod", date);
        }
        return SchemaTransformer.transform(restUtil.getForObject(uriComponents.build().toString(), GenericData.class));
    }

    public ExchangedAmount getFXValue(String currencyCode, String amount, String date) throws CurrencyNotSupportedException, RateNotAvailableException {
        Optional<ConversionRate> currencyConversionRate = getFXRates(currencyCode, date == null ? getCurrentDate() : date).getConversionRateList().stream().findFirst();
        var exchangedAmount = new ExchangedAmount();
        if (currencyConversionRate.isPresent()) {
            try {
                double conversionRate = Double.parseDouble(currencyConversionRate.get().getConversionRate());
                exchangedAmount.setConversionRate(currencyConversionRate.get());
                exchangedAmount.setAmount(String.valueOf(conversionRate * Double.parseDouble(amount)));
            } catch (NumberFormatException nfe) {
                throw new RateNotAvailableException(date);
            }
        }
        return exchangedAmount;
    }

    private void validateSupportedCurrency(String currencyCode) throws CurrencyNotSupportedException {
        val currencyStream = Arrays.stream(Currency.values());
        if (Currency.getCurrency(currencyCode).isEmpty()) {
            throw new CurrencyNotSupportedException(currencyCode);
        }
    }

    private String getCurrentDate() {
        return DATE_TIME_FORMATTER.format(LocalDate.now());
    }
}
