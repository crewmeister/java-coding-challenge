package com.crewmeister.cmcodingchallenge.xmldata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Observation {

    @JacksonXmlProperty(localName = "ObsDimension", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic")
    public ObsDimension dimension;

    @JacksonXmlProperty(localName = "ObsValue", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic")
    public ObsValue value;
}
