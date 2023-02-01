package com.crewmeister.cmcodingchallenge.currency.domain.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Obs {
    @JacksonXmlProperty(localName = "ObsDimension")
    ObsDimension obsDimension;

    @JacksonXmlProperty(localName = "ObsValue")
    ObsValue obsValue;
}
