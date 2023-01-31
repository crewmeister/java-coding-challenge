package com.crewmeister.cmcodingchallenge.currency.unit;

import com.crewmeister.cmcodingchallenge.currency.domain.external.GenericData;
import com.crewmeister.cmcodingchallenge.currency.util.RestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Currency;

public class TestRestUtil {

    @Autowired
    private RestUtil restUtil;



    String url1 = "https://api.statistiken.bundesbank.de/rest/data/BBEX3/D.PLN.EUR.BB.AC.000?detail=dataonly&lastNObservations=10";

    //@Test
    /*void testRestServiceUtil() throws IOException {
        GenericData value = restUtil.getForObject(url, GenericData.class);
        System.out.println(value.getDataSet());
    }*/

    @Test
    void testRestService() throws IOException {
        System.out.println(Currency.getAvailableCurrencies());
        GenericData value = new RestTemplate().getForObject(url1, GenericData.class);
        System.out.println(value.getDataSet());
    }
}
