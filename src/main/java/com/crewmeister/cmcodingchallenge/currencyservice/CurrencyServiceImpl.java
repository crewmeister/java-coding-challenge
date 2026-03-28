package com.crewmeister.cmcodingchallenge.currencyservice;

import com.crewmeister.cmcodingchallenge.currency.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.exception.InvalidRequestException;
import com.crewmeister.cmcodingchallenge.xmldata.GenericData;
import com.crewmeister.cmcodingchallenge.currency.Currency;
import com.crewmeister.cmcodingchallenge.currencyrepository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.xmldata.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketTimeoutException;
import java.util.*;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);
    private final RestTemplate restTemplate;

    private static final int RETRY_ATTEMPTS = 3;
    private static final long DELAY = 2000L;

    @Value("${bundesbank.api.base.url}")
    private String bundesBaseUrl;

    @Value("${bundesbank.api.key}")
    private String bundesApiKey;

    @Value("${bundesbank.api.format}")
    private String bundesApiFormat;

    @Value("${bundesbank.api.lang}")
    private String bundesApiLang;


    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(RestTemplate restTemplate, CurrencyRepository currencyRepository) {
        this.restTemplate = restTemplate;
        this.currencyRepository = currencyRepository;
    }


    @Cacheable("availableCurrencies")
    @Override
    public List<Currency> getListOfAvailableCurrencies() {
        return currencyRepository.findAll();
    }

    @CacheEvict(value = "availableCurrencies", allEntries = true)
    @Override
    public List<Currency> addCurrency(List<Currency> currency) {
        return currencyRepository.saveAll(currency);
    }

    @Cacheable(value = "currencyByName", key = "currencyName")
    public Optional<Currency> getCurrencyByName(String currencyName) {
        return currencyRepository.findCurrencyName(currencyName);
    }

    @Override
    public Map<String, Map<String, String>> getFXRates(String date,String currency) {
        List<Currency> currencies = new ArrayList<>();
        if(currency==null ||  currency.isEmpty() )
            currencies= getListOfAvailableCurrencies();//Assuming DB has values; if not insert values from controller
        else {
            Optional<Currency> ccy = getCurrencyByName(currency);
            if(ccy.isPresent())
                currencies.add(ccy.get());
            else{
                throw new RuntimeException("Invalid Currency Name as validated from db");
            }

        }
        Map<String, Map<String, String>> fxMapResult = new TreeMap<>();
        for(Currency fxcurrency: currencies) {
            logger.info("Processing currency: {}",fxcurrency.getCurrencyName());
            String url = buildUrl(fxcurrency.getCurrencyName());
            logger.info("Requesting FX data from URL: {} ", url);
            GenericData genericData = fetchRates(url, fxcurrency.getCurrencyName());
            if (genericData == null || genericData.dataSet == null || genericData.dataSet.series == null) {
                logger.warn("Received empty response for currency {}", fxcurrency.getCurrencyName());
                continue;
            }
            Map<String, String> fxMap = new TreeMap<>();


            for (Observation obs : genericData.dataSet.series.observations) {
                if (date == null || date.isEmpty()) {
                    if (obs.value == null)
                        continue;
                    fxMap.put(obs.dimension.date, obs.value.rate);
                } else {
                    if (obs.dimension.date.equals(date) && obs.value != null) {
                        fxMap.put(obs.dimension.date, obs.value.rate);
                        break;
                    }
                    else if (obs.dimension.date.equals(date) && obs.value == null) {
                        fxMap.put(date, "Rate cannot be fetched as it is weekend or public holiday");
                        break;
                    }
                }
            }
            fxMapResult.put(fxcurrency.getCurrencyName(), fxMap);
        }

        return fxMapResult;
    }

    @Override
    public double getFXAmount(String date, String currency, double amount) {

        logger.info("Processing currency: {}",currency);
        CurrencyConversionRates ccr = null;
        String url = buildUrl(currency);
        logger.info("Requesting FX data from URL: {} ", url);
        GenericData genericData = fetchRates(url,currency);
        if (genericData == null || genericData.dataSet == null || genericData.dataSet.series == null) {
            logger.error("Received empty response for currency {}", currency);
            throw new RuntimeException("Generic data is unavailable for currency: " + currency);
        }
        for (Observation obs : genericData.dataSet.series.observations) {
            if (obs.dimension.date.equals(date) && obs.value != null) {
                ccr = new CurrencyConversionRates(Double.parseDouble(obs.value.rate));
                break;
            }
        }

        if (ccr == null) {
            throw new IllegalArgumentException("Conversion rate not found for date " + date);
        }
        logger.info("Conversion rate for {} on {} is {}",currency,date,ccr.getConversionRate());
        double exchangedAmount = new BigDecimal(amount/ccr.getConversionRate()).setScale(4, RoundingMode.HALF_UP).doubleValue() ;

        return exchangedAmount;
    }

    private String buildUrl(String currencyName) {
        String apiKey = String.format(bundesApiKey, currencyName);
        return UriComponentsBuilder
                .fromHttpUrl(bundesBaseUrl.endsWith("/") ? bundesBaseUrl : bundesBaseUrl + "/")
                .pathSegment(apiKey)
                .queryParam("format", bundesApiFormat)
                .queryParam("lang", bundesApiLang)
                .build()
                .toUriString();
    }
    @Retryable(
            value = { ResourceAccessException.class, SocketTimeoutException.class },
            maxAttempts = RETRY_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    private GenericData fetchRates(String url,String currency) {
        try {
            var response = restTemplate.getForEntity(url, GenericData.class);
            GenericData genericData = response.getBody();
            return genericData;
        }
        catch (Exception e) {
            logger.error("Failed to fetch data from Bundesbank for currency {}: {}", currency, e.getMessage());
            throw new InvalidRequestException("Unable to retrieve FX rate data, please check logs for more details");
        }

    }

}
