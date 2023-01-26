package com.crewmeister.cmcodingchallenge.schema.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Header {
    @JsonProperty("ID")
    String id;
}
