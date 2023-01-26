package com.crewmeister.cmcodingchallenge.schema.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Series {
    @JacksonXmlProperty(localName ="SeriesKey")
    SeriesKey seriesKey;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName ="Obs")
    List<Obs> obsList;
}
