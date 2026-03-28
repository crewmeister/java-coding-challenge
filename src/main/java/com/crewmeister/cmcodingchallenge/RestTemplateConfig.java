package com.crewmeister.cmcodingchallenge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${rest.template.connection.timeout}")
    private int connectionTimeout;

    @Value("${rest.template.read.timeout}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout); // in milliseconds
        factory.setReadTimeout(readTimeout); // in milliseconds
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new MappingJackson2XmlHttpMessageConverter());
        return restTemplate;
    }
}
