package com.crewmeister.cmcodingchallenge.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestUtil {
    @Autowired
    private RestTemplate restTemplate;

    public <T> T getForObject(String url, Class<T> typeC) {
        log.info("invoking:" + url);
        return restTemplate.getForObject(url, typeC);
    }
}

