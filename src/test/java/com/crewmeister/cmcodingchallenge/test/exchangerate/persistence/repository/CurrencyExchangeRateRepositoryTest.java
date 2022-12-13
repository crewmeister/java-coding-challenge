package com.crewmeister.cmcodingchallenge.test.exchangerate.persistence.repository;

import static java.lang.String.format;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.crewmeister.cmcodingchallenge.exchangerate.persistence.entity.CurrencyExchangeRateEntity;
import com.crewmeister.cmcodingchallenge.exchangerate.persistence.repository.CurrencyExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.exchangerate.persistence.repository.impl.CurrencyExchangeRateRepositoryImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//@DataJpaTest
public class CurrencyExchangeRateRepositoryTest {

//    @Autowired
    private final CurrencyExchangeRateRepository repository = new CurrencyExchangeRateRepositoryImpl();

    @BeforeEach
    public void setUp() {
        repository.saveAll(List.of(
                new CurrencyExchangeRateEntity(of(2020, 1, 1), "AUD", 1.1D),
                new CurrencyExchangeRateEntity(of(2021, 1, 1), "AUD", 1.2D),
                new CurrencyExchangeRateEntity(of(2022, 1, 1), "AUD", 1.3D),
                new CurrencyExchangeRateEntity(of(2020, 1, 1), "USD", 1.2D),
                new CurrencyExchangeRateEntity(of(2020, 1, 1), "CAD", 1.3D)
        ));
    }

    @Test
    void testFindDistinctCurrencyCode_returnsUniqueSetOfCurrencyCodes() {
        List<String> expected = List.of("AUD", "USD", "CAD");

        List<String> actual = repository.findDistinctCurrencyCode();

        assertNotNull(actual, "findDistinctCurrencyCode returned null");
        assertEquals(actual.size(), expected.size(),
                format("findDistinctCurrencyCode returned list of size %d, expected %d", actual.size(), expected.size()));
        assertTrue(expected.containsAll(actual), format("findDistinctCurrencyCode returned different list; expected %s, found %s", expected, actual));
    }

    @Test
    void testFindByDate_returnsExchangeRatesForDay() {
        List<CurrencyExchangeRateEntity> expected = List.of(
                new CurrencyExchangeRateEntity(of(2020, 1, 1), "AUD", 1.1D),
                new CurrencyExchangeRateEntity(of(2020, 1, 1), "USD", 1.2D),
                new CurrencyExchangeRateEntity(of(2020, 1, 1), "CAD", 1.3D)
        );

        List<CurrencyExchangeRateEntity> actual = repository.findByDate(of(2020, 1, 1));

        assertNotNull(actual, "findByDate returned null");
        assertEquals(actual.size(), expected.size(),
                format("findByDate returned list of size %d, expected %d", actual.size(), expected.size()));
        assertTrue(expected.containsAll(actual), format("findByDate returned different list; expected %s, found %s", expected, actual));
    }

    @Test
    void testFindByDateCurrencyCode_returnsExchangeRate() {
        Double expected = 1.1D;

        CurrencyExchangeRateEntity actual = repository.findByDateAndCurrencyCode(of(2020, 1, 1), "AUD");

        assertNotNull(actual, "findByDate returned null");
        Double actualExchangeRate = actual.getExchangeRate();
        assertEquals(expected, actualExchangeRate, format("findByDateAndCurrencyCode returned different result; expected %s, found %s", expected, actualExchangeRate));
    }

    @Test
    void testFindByUnExpectedDateCurrencyCode_returnsNull() {
        CurrencyExchangeRateEntity actual = repository.findByDateAndCurrencyCode(of(2023, 1, 1), "AUD");

        assertNull(actual, "findByDate returned not null, expected null");
    }
}