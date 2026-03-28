package com.crewmeister.cmcodingchallenge.controllertests;

import com.crewmeister.cmcodingchallenge.security.MyUserDetailsService;
import com.crewmeister.cmcodingchallenge.currency.CurrencyConstants;
import com.crewmeister.cmcodingchallenge.currencycontroller.CurrencyController;
import com.crewmeister.cmcodingchallenge.currencyservice.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.crewmeister.cmcodingchallenge.currency.Currency;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CurrencyService currencyService;

    @MockBean
    private MyUserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAddCurrencyWithNoCurrencies() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/cmfxapi/addCurrencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Currency list must not be null or empty\"}"));
    }

    @Test
    public void testAddCurrencyWithInvalidInputCurrencies() throws Exception {
        String payLoad="{\n" +
                "  \"currencies\": [\n" +
                "    {\n" +
                "\t\t\"currencyId\": 1,\n" +
                "\t\t\"currencyName\":\"AUD\"\n" +
                "},]\n" + //extra comma is added for malforming json
                "}";

        mvc.perform(MockMvcRequestBuilders
                        .post("/cmfxapi/addCurrencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoad)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Invalid json input\"}"));
    }

    @Test
    public void testAddCurrencyWithValidInputCurrencies() throws Exception {
        String payLoad="{\n" +
                "  \"currencies\": [\n" +
                "    {\n" +
                "\t\t\"currencyId\": 1,\n" +
                "\t\t\"currencyName\":\"AUD\"\n" +
                "},\n" +
                "{\n" +
                "\t\t\"currencyId\": 2,\n" +
                "\t\t\"currencyName\":\"BGN\"\n" +
                "}]\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders
                        .post("/cmfxapi/addCurrencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoad)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAvailableCurrenciesFromDB() throws Exception {
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(new Currency(1L, "AUD"));
        currencyList.add(new Currency(2L, "CHF"));

        String expected ="[\"AUD\",\"CHF\"]";

        Mockito.when(currencyService.getListOfAvailableCurrencies()).thenReturn(currencyList);

        mvc.perform(MockMvcRequestBuilders
                .get("/cmfxapi/getAllAvailableCurrencies"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void testGetAvailableCurrenciesFromEnum() throws Exception {
        Mockito.when(currencyService.getListOfAvailableCurrencies()).thenReturn(Collections.emptyList());

        List<String> enumCurrencies = Arrays.stream(CurrencyConstants.values())
                                .map(Enum::name)
                                .collect(Collectors.toList());

        String expected = new ObjectMapper().writeValueAsString(enumCurrencies);
        mvc.perform(MockMvcRequestBuilders
                .get("/cmfxapi/getAllAvailableCurrencies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void testGetAllFXRatesWhenDateAndCurrencyAreNull() throws Exception {
        Map<String, Map<String, String>> fxRateMock = Map.of(
                "GBP", Map.of("2025-05-16", "0.84270"),
                "INR", Map.of("2025-05-15", "95.8200")
        );
        Mockito.when(currencyService.getFXRates(null, null)).thenReturn(fxRateMock);

        String expected = new ObjectMapper().writeValueAsString(fxRateMock);

        mvc.perform(MockMvcRequestBuilders
                 .get("/cmfxapi/getAllFXRates")
                 .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void testGetAllFXRatesWhenDateExistsAndCurrencyIsNull() throws Exception {
        String date = "2025-05-16";
        Map<String, Map<String, String>> fxRateMock = Map.of(
                "GBP", Map.of("2025-05-16", "0.84270"),
                "INR", Map.of("2025-05-16", "95.8200")
        );
        Mockito.when(currencyService.getFXRates(date, null)).thenReturn(fxRateMock);

        String expected = new ObjectMapper().writeValueAsString(fxRateMock);

        mvc.perform(MockMvcRequestBuilders
                 .get("/cmfxapi/getAllFXRates")
                 .param("date", date)
                 .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void testGetAllFXRatesWhenDateisNullAndCurrencyExists() throws Exception {
        String currency = "INR";
        Map<String, Map<String, String>> fxRateMock = Map.of(
                "INR", Map.of("2025-05-16", "0.84270","2025-05-15","95.6470")
        );
        Mockito.when(currencyService.getFXRates(null, currency)).thenReturn(fxRateMock);

        String expected = new ObjectMapper().writeValueAsString(fxRateMock);

        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getAllFXRates")
                        .param("currency", currency)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void testGetAllFXRatesWhenDateAndCurrencyExists() throws Exception {
        String currency = "INR";
        String date = "2025-05-16";
        Map<String, Map<String, String>> fxRateMock = Map.of(
                "INR", Map.of("2025-05-16", "0.84270")
        );
        Mockito.when(currencyService.getFXRates(date, currency)).thenReturn(fxRateMock);

        String expected = new ObjectMapper().writeValueAsString(fxRateMock);

        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getAllFXRates")
                        .param("date", date)
                        .param("currency", currency)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void testGetAllFXRatesWithInvalidDateFormat() throws Exception {
        String expected="{\"error\":\"Invalid value '05-05-2025' for parameter 'date'. Expected format is yyyy-MM-dd and type: LocalDate\"}";
        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getAllFXRates")
                        .param("date", "05-05-2025") // invalid format
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expected));
    }

    @Test
    void testGetFXAmountWithNullDateInput() throws Exception {
        String expected="{\"date\":\"Date is required\"}";
        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getFXAmount")
                        .param("date", "")
                        .param("currency", "INR")
                        .param("amount", "500.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expected));
    }

    @Test
    void testGetFXAmountWithNullCurrencyInput() throws Exception {
        String expected="{\"currency\":\"Currency is required\"}";
        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getFXAmount")
                        .param("date", "2025-05-16")
                        .param("currency", "")
                        .param("amount", "500.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expected));
    }
    @Test
    void testGetFXAmountWithNullAmountInput() throws Exception {
        String expected="{\"amount\":\"Failed to convert property value of type 'java.lang.String' to required type 'double' for property 'amount'; nested exception is java.lang.NumberFormatException: empty String\"}";
        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getFXAmount")
                        .param("date", "2025-05-16")
                        .param("currency", "INR")
                        .param("amount", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expected));
    }
    @Test
    void testGetFXAmountWithInValidDateInput() throws Exception {
        String expected="{\"date\":\"Date must be a LocalDate in format yyyy-MM-dd\"}";
        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getFXAmount")
                        .param("date", "16-05-2025")
                        .param("currency", "INR")
                        .param("amount", "500.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expected));
    }
    @Test
    void testGetFXAmountWithInValidCurrencyInput() throws Exception {
        String expected="{\"currency\":\"Failed to convert property value of type 'java.lang.String' to required type 'com.crewmeister.cmcodingchallenge.currency.CurrencyConstants' for property 'currency'; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.String] to type [@javax.validation.constraints.NotNull com.crewmeister.cmcodingchallenge.currency.CurrencyConstants] for value 'INS'; nested exception is java.lang.IllegalArgumentException: No enum constant com.crewmeister.cmcodingchallenge.currency.CurrencyConstants.INS\"}";
        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getFXAmount")
                        .param("date", "2025-05-16")
                        .param("currency", "INS")
                        .param("amount", "500.0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expected));
    }
    @Test
    void testGetFXAmountWithZeroAmountInput() throws Exception {
        String expected="{\"amount\":\"Amount must be a positive number\"}";
        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getFXAmount")
                        .param("date", "2025-05-16")
                        .param("currency", "INR")
                        .param("amount", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expected));
    }

    @Test
    void testGetFXAmountWithNegativeAmountInput() throws Exception {
        String expected="{\"amount\":\"Amount must be a positive number\"}";
        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getFXAmount")
                        .param("date", "2025-05-16")
                        .param("currency", "INR")
                        .param("amount", "-500"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expected));
    }
    @Test
    void testGetFXAmountWithValidInputs() throws Exception {
        double expectedAmount = 5.1966;

        Mockito.when(currencyService.getFXAmount("2025-05-16", "INR", 500.0))
                .thenReturn(expectedAmount);

        mvc.perform(MockMvcRequestBuilders
                        .get("/cmfxapi/getFXAmount")
                        .param("date", "2025-05-16")
                        .param("currency", "INR")
                        .param("amount", "500.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("5.1966"));

    }

    @Test
    void givenDifferentUrlRequiresAuthCheck() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/secure/test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

}
