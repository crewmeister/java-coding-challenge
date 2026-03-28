package com.crewmeister.cmcodingchallenge.xmldata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class DataSet {

    @JacksonXmlProperty(localName = "Series", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/generic")
    public Series series;
}
