package com.crewmeister.cmcodingchallenge.xmldata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Series {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Obs", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic")
    public List<Observation> observations;
}
