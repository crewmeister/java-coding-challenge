package com.crewmeister.cmcodingchallenge.currency.integration;

import com.crewmeister.cmcodingchallenge.currency.domain.external.*;
import com.crewmeister.cmcodingchallenge.currency.domain.internal.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.currency.domain.internal.CurrencyResponse;
import com.crewmeister.cmcodingchallenge.currency.domain.internal.ExchangedAmount;
import com.crewmeister.cmcodingchallenge.currency.util.RestUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestCurrencyController {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private RestUtil restUtil;

    @Test
    public void testGetCurrenciesSuccess() {
        var response = this.testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/currencies",
                CurrencyResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(42, Objects.requireNonNull(response.getBody()).getCurrencies().length);
    }

    @Test
    public void testGetExchangeRateForDateSuccess() {
        String targetUrl = "https://api.statistiken.bundesbank.de/rest/data/BBEX3/D.CHF.EUR.BB.AC.000?detail=dataonly&startPeriod=2023-01-29&endPeriod=2023-01-29";

        Mockito.when(restUtil.getForObject(targetUrl, GenericData.class)).thenReturn(createDummyResponse("2023-01-24", "1.4517"));
        var response = this.testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/exchange-rates?date=2023-01-29&currencyCode=CHF", CurrencyConversionRates.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, Objects.requireNonNull(response.getBody()).getConversionRateList().size());
        assertEquals("1.4517", response.getBody().getConversionRateList().get(0).getRate());
    }

    @Test
    public void testGetCurrencyExchangeSuccess() {//"2023-01-24" //"1.4517"
        String targetUrl = "https://api.statistiken.bundesbank.de/rest/data/BBEX3/D.CHF.EUR.BB.AC.000?detail=dataonly&startPeriod=2023-01-29&endPeriod=2023-01-29";

        Mockito.when(restUtil.getForObject(targetUrl, GenericData.class)).thenReturn(createDummyResponse("2023-01-24", "1.4517"));
        var response = this.testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/exchange-currency?date=2023-01-29&currencyCode=CHF&amount=2000", ExchangedAmount.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(Objects.requireNonNull(response.getBody()).getAmount());
        assertEquals("1.4517", Objects.requireNonNull(response.getBody()).getConversionRate().getRate());
        assertEquals("2903.4", Objects.requireNonNull(response.getBody()).getAmount());
    }

    @Test
    public void testThrowRateNotAvailableException() {
        String targetUrl = "https://api.statistiken.bundesbank.de/rest/data/BBEX3/D.CHF.EUR.BB.AC.000?detail=dataonly&startPeriod=2023-01-28&endPeriod=2023-01-28";

        Mockito.when(restUtil.getForObject(targetUrl, GenericData.class)).thenReturn(createDummyResponse("2023-01-28", null));
        var response = this.testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/exchange-currency?date=2023-01-28&currencyCode=CHF&amount=2000", ExchangedAmount.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetExchangeRateForNotSupportedCurrency() {
        var response = this.testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/exchange-rates?date=2023-01-29&currencyCode=AED", CurrencyConversionRates.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private GenericData createDummyResponse(String date, String value) {
        var obsDimension = new ObsDimension(date);
        var obsValue = value == null ? null : new ObsValue(value);
        var obsList = List.of(new Obs(obsDimension, obsValue));
        var series = Series.builder()
                .seriesKey(SeriesKey.builder().valueList(List.of(new com.crewmeister.cmcodingchallenge.currency.domain.external.Value("BBK_STD_CURRENCY", "CHF"))).build())
                .obsList(obsList).build();
        return GenericData.builder()
                .dataSet(DataSet.builder().series(series).build())
                .build();
    }
}
