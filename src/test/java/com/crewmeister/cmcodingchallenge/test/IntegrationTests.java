package com.crewmeister.cmcodingchallenge.test;

import static java.lang.String.format;
import static java.time.LocalDate.of;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.Map;

import com.crewmeister.cmcodingchallenge.exchangerate.model.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.exchangerate.model.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testgetAllCurrencies_returnsAllCurrencies(){
        ResponseEntity<List> response = restTemplate.getForEntity("/api/currencies", List.class);
        assertEquals(response.getStatusCode(), OK);
    }

    @Test
    public void testGetAllExchangeRates_returnsAllExchangeRates() {
        ResponseEntity<Response> response = restTemplate.getForEntity("/api/exchangerates", Response.class);

        assertEquals(response.getStatusCode(), OK);

        Response body = response.getBody();
        assertNotNull(body, "Response body is null");
        assertEquals(body.getMessage(), "ok", format("Expected response message to be 'ok', found '%s'", body.getMessage()));
        assertNotNull(body.getData(), "Response data is null");
        assertTrue(body.getData() instanceof List, format("Expected response data expected List; found %s", body.getData().getClass()));

        List<CurrencyExchangeRate> rates = (List<CurrencyExchangeRate>) body.getData();
        assertTrue(isNotEmpty(rates), "Exchange Rates are empty");
    }

    @Test
    public void testGetExchangeRatesForDay_returnsAllExchangeRatesForTheDay() {
        String dateStrFormat = of(2020, 1, 23).format(ISO_DATE);
        ResponseEntity<Response> response = restTemplate
                .getForEntity(format("/api/exchangerates/date/%s", dateStrFormat), Response.class);

        assertEquals(response.getStatusCode(), OK);

        Response body = response.getBody();
        assertNotNull(body, "Response body is null");

        assertEquals(body.getMessage(), "ok", format("Expected response message to be 'ok', found '%s'", body.getMessage()));

        assertNotNull(body.getData(), "Response data is null");

        assertTrue(body.getData() instanceof List, format("Expected response data expected List; found %s", body.getData().getClass()));

        List<Map<String, Object>> rates = (List<Map<String, Object>>) body.getData();

        assertTrue(isNotEmpty(rates), "Exchange Rates are empty");

        assertTrue(rates.stream().anyMatch(e -> dateStrFormat.equals(e.get("date"))), "Exchange Rates are empty");
    }

    @Test
    public void testGetExchangeRatesForDayAndCurrency_returnsExchangeRateForTheDayAndCurrency() {
        String dateStrFormat = of(2020, 1, 23).format(ISO_DATE);
        ResponseEntity<Response> response = restTemplate
                .getForEntity(format("/api/exchangerates/date/%s/currency/AUD", dateStrFormat), Response.class);

        assertEquals(response.getStatusCode(), OK);

        Response body = response.getBody();
        assertNotNull(body, "Response body is null");

        assertEquals(body.getMessage(), "ok", format("Expected response message to be 'ok', found '%s'", body.getMessage()));

        assertNotNull(body.getData(), "Response data is null");

        assertTrue(body.getData() instanceof Double, format("Expected response data expected Double; found %s", body.getData().getClass()));

        Double rate = (Double) body.getData();

        assertEquals(Double.valueOf("1.6149"), rate, "Exchange Rates are empty");
    }
}
