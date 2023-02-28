package com.crewmeister.cmcodingchallenge.currency.domain.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Header {
    @JsonProperty("ID")
    private String id;
}
