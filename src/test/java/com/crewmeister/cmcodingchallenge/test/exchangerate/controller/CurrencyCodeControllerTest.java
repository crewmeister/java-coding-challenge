package com.crewmeister.cmcodingchallenge.test.exchangerate.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.crewmeister.cmcodingchallenge.exchangerate.controller.CurrencyController;
import com.crewmeister.cmcodingchallenge.exchangerate.service.ExchangeRateService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CurrencyController.class)
public class CurrencyCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    public void getCurrencies_returnsAllCurrencyCodes() throws Exception {
        given(exchangeRateService.getAllCurrencies()).willReturn(List.of("AUD", "USD", "PND"));

        mockMvc.perform(get("/api/currencies")).
                andExpect(status().isOk()).
                andExpect(jsonPath("[0]").value("AUD")).
                andExpect(jsonPath("[1]").value("USD")).
                andExpect(jsonPath("[2]").value("PND"))
        ;
    }
}
