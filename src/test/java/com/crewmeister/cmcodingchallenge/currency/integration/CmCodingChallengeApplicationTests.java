package com.crewmeister.cmcodingchallenge.currency.integration;

import com.crewmeister.cmcodingchallenge.currency.controller.CurrencyController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CmCodingChallengeApplicationTests {

    @Test
    void contextLoads(ApplicationContext context) {
        assertNotNull(context);
        assertNotNull(context.getBean(CurrencyController.class));
    }

}
