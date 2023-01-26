package com.crewmeister.cmcodingchallenge.schema.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Obs {
    @JacksonXmlProperty(localName ="ObsDimension")
    ObsDimension obsDimension;

    @JacksonXmlProperty(localName ="ObsValue")
    ObsValue obsValue;
}
