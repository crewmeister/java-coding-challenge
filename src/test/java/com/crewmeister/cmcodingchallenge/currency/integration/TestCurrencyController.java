package com.crewmeister.cmcodingchallenge.currency.integration;

import com.crewmeister.cmcodingchallenge.currency.domain.external.*;
import com.crewmeister.cmcodingchallenge.currency.domain.internal.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.currency.domain.internal.CurrencyResponse;
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

        Mockito.when(restUtil.getForObject(targetUrl, GenericData.class)).thenReturn(createDummyResponse());
        var response = this.testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/exchange-rates?date=2023-01-29&currencyCode=CHF", CurrencyConversionRates.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, Objects.requireNonNull(response.getBody()).getConversionRateList().size());
        assertEquals("1.4517", response.getBody().getConversionRateList().get(0).getConversionRate());
    }

    @Test
    public void testGetExchangeRateForDateBadRequest() {
        String targetUrl = "https://api.statistiken.bundesbank.de/rest/data/BBEX3/D.AED.EUR.BB.AC.000?detail=dataonly&startPeriod=2023-01-29&endPeriod=2023-01-29";

        Mockito.when(restUtil.getForObject(targetUrl, GenericData.class)).thenReturn(createDummyResponse());
        var response = this.testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/exchange-rates?date=2023-01-29&currencyCode=INR", CurrencyConversionRates.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getConversionRateList().size());
        assertEquals("1.4517", response.getBody().getConversionRateList().get(0).getConversionRate());
    }

    private GenericData createDummyResponse() {
        var obsList = List.of(new Obs(new ObsDimension("2023-01-24"), new ObsValue("1.4517")));
        var series = Series.builder()
                .seriesKey(SeriesKey.builder().valueList(List.of(new com.crewmeister.cmcodingchallenge.currency.domain.external.Value("BBK_STD_CURRENCY", "CHF"))).build())
                .obsList(obsList).build();
        var genericData =
                GenericData.builder()
                        .dataSet(DataSet.builder().series(series).build())
                        .build();
        return genericData;
    }
}
