package com.crewmeister.cmcodingchallenge.util;

import com.crewmeister.cmcodingchallenge.CmCodingChallengeApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUtil {
    private static final Logger log = LoggerFactory.getLogger(CmCodingChallengeApplication.class);
    @Autowired
    private RestTemplate restTemplate;

    public <T> T getForObject(String url, Class<T> typeC) {
        log.info("invoking:"+ url);
        return restTemplate.getForObject(url, typeC);
    }
}

