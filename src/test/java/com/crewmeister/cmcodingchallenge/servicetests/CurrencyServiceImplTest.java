package com.crewmeister.cmcodingchallenge.servicetests;

import com.crewmeister.cmcodingchallenge.currency.Currency;
import com.crewmeister.cmcodingchallenge.currencyrepository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.currencyservice.CurrencyServiceImpl;
import com.crewmeister.cmcodingchallenge.exception.InvalidRequestException;
import com.crewmeister.cmcodingchallenge.xmldata.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceImplTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(currencyService, "bundesBaseUrl", "https://api.statistiken.bundesbank.de/rest/download/");
        ReflectionTestUtils.setField(currencyService, "bundesApiKey", "BBEX3/D.%s.EUR.BB.AC.000");
        ReflectionTestUtils.setField(currencyService, "bundesApiFormat", "sdmx");
        ReflectionTestUtils.setField(currencyService, "bundesApiLang", "en");
    }

    @Test
    void testGetListOfAvailableCurrencies() {
        List<Currency> mockList = List.of(new Currency(1L, "AUD"), new Currency(2L, "BGN"));
        when(currencyRepository.findAll()).thenReturn(mockList);

        List<Currency> result = currencyService.getListOfAvailableCurrencies();
        assertEquals(2, result.size());
        verify(currencyRepository, times(1)).findAll();
    }

    @Test
    void testAddCurrency() {
        List<Currency> input = List.of(new Currency(3L, "BRL"));
        when(currencyRepository.saveAll(input)).thenReturn(input);

        List<Currency> result = currencyService.addCurrency(input);
        assertEquals("BRL", result.get(0).getCurrencyName());
        verify(currencyRepository, times(1)).saveAll(input);
    }

    @Test
    void testGetCurrencyByName() {
        Currency currency = new Currency(4L, "CAD");
        when(currencyRepository.findCurrencyName("CAD")).thenReturn(Optional.of(currency));

        Optional<Currency> result = currencyService.getCurrencyByName("CAD");
        assertTrue(result.isPresent());
        assertEquals("CAD", result.get().getCurrencyName());
    }

    @Test
    void testGetFXRatesWithValidCurrencyAndDate() {
        Currency ccy = new Currency(1L, "INR");
        when(currencyRepository.findCurrencyName("INR")).thenReturn(Optional.of(ccy));

        ObsValue obsValue = new ObsValue();
        obsValue.rate="95.8200";
        ObsDimension obsDimension = new ObsDimension();
        obsDimension.date="2025-05-16";

        Observation obs = new Observation();
        obs.value = obsValue;
        obs.dimension = obsDimension;

        Series series = new Series();
        series.observations = List.of(obs);

        DataSet dataSet = new DataSet();
        dataSet.series = series;

        GenericData genericData = new GenericData();
        genericData.dataSet = dataSet;



        when(restTemplate.getForEntity(anyString(), eq(GenericData.class)))
                .thenReturn(new ResponseEntity<>(genericData, HttpStatus.OK));

        Map<String, Map<String, String>> result = currencyService.getFXRates("2025-05-16", "INR");

        assertTrue(result.containsKey("INR"));
        assertEquals("95.8200", result.get("INR").get("2025-05-16"));
    }

    @Test
    void testGetFXRatesWithValidCurrencyAndWeekendDate() {
        Currency ccy = new Currency(1L, "INR");
        when(currencyRepository.findCurrencyName("INR")).thenReturn(Optional.of(ccy));


        ObsDimension obsDimension = new ObsDimension();
        obsDimension.date="2025-05-16";

        Observation obs = new Observation();
        obs.dimension = obsDimension;

        Series series = new Series();
        series.observations = List.of(obs);

        DataSet dataSet = new DataSet();
        dataSet.series = series;

        GenericData genericData = new GenericData();
        genericData.dataSet = dataSet;



        when(restTemplate.getForEntity(anyString(), eq(GenericData.class)))
                .thenReturn(new ResponseEntity<>(genericData, HttpStatus.OK));

        Map<String, Map<String, String>> result = currencyService.getFXRates("2025-05-16", "INR");

        assertTrue(result.containsKey("INR"));
        assertEquals("Rate cannot be fetched as it is weekend or public holiday", result.get("INR").get("2025-05-16"));
    }

    @Test
    void testGetFXRatesWithValidDateAndCurrencyAsNull() {
        List<Currency> mockList = List.of(new Currency(1L, "INR"), new Currency(2L, "GBP"));
        when(currencyService.getListOfAvailableCurrencies()).thenReturn(mockList);

        ObsValue obsValue1 = new ObsValue();
        obsValue1.rate="95.8201";
        ObsDimension obsDimension1 = new ObsDimension();
        obsDimension1.date="2025-05-16";

        ObsValue obsValue2 = new ObsValue();
        obsValue2.rate="0.8427";
        ObsDimension obsDimension2 = new ObsDimension();
        obsDimension2.date="2025-05-16";

        Observation obs1 = new Observation();
        obs1.dimension = obsDimension1;
        obs1.value = obsValue1;

        Observation obs2 = new Observation();
        obs2.dimension = obsDimension2;
        obs2.value = obsValue2;

        Series series1 = new Series();
        Series series2 = new Series();
        series1.observations = List.of(obs1);
        series2.observations = List.of(obs2);

        DataSet dataSet1 = new DataSet();
        DataSet dataSet2 = new DataSet();
        dataSet1.series = series1;
        dataSet2.series = series2;

        GenericData genericData1 = new GenericData();
        GenericData genericData2 = new GenericData();
        genericData1.dataSet = dataSet1;
        genericData2.dataSet = dataSet2;

        when(restTemplate.getForEntity(anyString(), eq(GenericData.class)))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0, String.class);
                    if (url.contains("INR")) {
                        return new ResponseEntity<>(genericData1, HttpStatus.OK);
                    } else if (url.contains("GBP")) {
                        return new ResponseEntity<>(genericData2, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                });

        Map<String, Map<String, String>> result = currencyService.getFXRates("2025-05-16", "");
        System.out.println(result);
        assertEquals(2, result.size());
        assertEquals(result.get("INR").get("2025-05-16"),"95.8201");
        assertEquals(result.get("GBP").get("2025-05-16"),"0.8427");
    }

    @Test
    void testGetFXRatesWithDateAndCurrencyBothAsNull() {
        List<Currency> mockList = List.of(new Currency(1L, "INR"), new Currency(2L, "GBP"));
        when(currencyService.getListOfAvailableCurrencies()).thenReturn(mockList);

        ObsValue obsValue1 = new ObsValue();
        obsValue1.rate="95.8201";
        ObsDimension obsDimension1 = new ObsDimension();
        obsDimension1.date="2025-05-15";

        ObsValue obsValue2 = new ObsValue();
        obsValue2.rate="0.8427";
        ObsDimension obsDimension2 = new ObsDimension();
        obsDimension2.date="2025-05-16";

        Observation obs1 = new Observation();
        obs1.dimension = obsDimension1;
        obs1.value = obsValue1;

        Observation obs2 = new Observation();
        obs2.dimension = obsDimension2;
        obs2.value = obsValue2;

        Series series1 = new Series();
        Series series2 = new Series();
        series1.observations = List.of(obs1);
        series2.observations = List.of(obs2);

        DataSet dataSet1 = new DataSet();
        DataSet dataSet2 = new DataSet();
        dataSet1.series = series1;
        dataSet2.series = series2;

        GenericData genericData1 = new GenericData();
        GenericData genericData2 = new GenericData();
        genericData1.dataSet = dataSet1;
        genericData2.dataSet = dataSet2;

        when(restTemplate.getForEntity(anyString(), eq(GenericData.class)))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0, String.class);
                    if (url.contains("INR")) {
                        return new ResponseEntity<>(genericData1, HttpStatus.OK);
                    } else if (url.contains("GBP")) {
                        return new ResponseEntity<>(genericData2, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                });

        Map<String, Map<String, String>> result = currencyService.getFXRates(null, "");
        assertEquals(2, result.size());
        assertEquals("95.8201",result.get("INR").get("2025-05-15"));
        assertEquals("0.8427",result.get("GBP").get("2025-05-16"));
    }

    @Test
    void testGetFXAmountWithValidInputs() {
        ObsValue obsValue = new ObsValue();
        obsValue.rate="95.8200";
        ObsDimension obsDimension = new ObsDimension();
        obsDimension.date="2025-05-16";

        Observation obs = new Observation();
        obs.dimension = obsDimension;
        obs.value = obsValue;

        Series series = new Series();
        series.observations = List.of(obs);

        DataSet dataSet = new DataSet();
        dataSet.series = series;

        GenericData genericData = new GenericData();
        genericData.dataSet = dataSet;



        when(restTemplate.getForEntity(anyString(), eq(GenericData.class)))
                .thenReturn(new ResponseEntity<>(genericData, HttpStatus.OK));

        double result = currencyService.getFXAmount("2025-05-16","INR",500);
        assertEquals(5.2181,result);
    }

    @Test
    void testGetFXAmountWithGenericDataAsNull() {
        GenericData genericData = new GenericData();
        when(restTemplate.getForEntity(anyString(), eq(GenericData.class)))
                .thenReturn(new ResponseEntity<>(genericData, HttpStatus.OK));
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                currencyService.getFXAmount("2025-05-16", "INR", 500.00)
        );

        assertEquals("Generic data is unavailable for currency: INR", exception.getMessage());
    }

    @Test
    void testGetFXAmountWithRateAsNull() {

        ObsDimension obsDimension = new ObsDimension();
        obsDimension.date="2025-05-16";

        Observation obs = new Observation();
        obs.dimension = obsDimension;
        obs.value = null;

        Series series = new Series();
        series.observations = List.of(obs);

        DataSet dataSet = new DataSet();
        dataSet.series = series;

        GenericData genericData = new GenericData();
        genericData.dataSet = dataSet;



        when(restTemplate.getForEntity(anyString(), eq(GenericData.class)))
                .thenReturn(new ResponseEntity<>(genericData, HttpStatus.OK));

        RuntimeException exception = assertThrows(IllegalArgumentException.class, () ->
                currencyService.getFXAmount("2025-05-16", "INR", 500.00)
        );

        assertEquals("Conversion rate not found for date 2025-05-16", exception.getMessage());
    }

    @Test
    void testFetchRatesThrowsException() {
        when(restTemplate.getForEntity(anyString(), eq(GenericData.class)))
                .thenThrow(new RestClientException("Invalid URL"));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                currencyService.getFXAmount("2025-05-16", "INR", 500.00)
        );

        assertEquals("Unable to retrieve FX rate data, please check logs for more details", exception.getMessage());
    }

    @Test
    void testRestTemplateTimeout() {
        when(currencyRepository.findCurrencyName("INR"))
                .thenReturn(Optional.of(new Currency(1L, "INR")));
        when(restTemplate.getForEntity(anyString(), eq(GenericData.class)))
                .thenThrow(new ResourceAccessException("Read timed out"));

        assertThrows(
                RuntimeException.class,
                () -> currencyService.getFXRates("2025-05-16", "INR"),
                "Expected timeout to result in RuntimeException"
        );

        verify(restTemplate, atLeast(1)).getForEntity(anyString(), eq(GenericData.class));
    }


}

