package com.crewmeister.cmcodingchallenge.xmldata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "GenericData", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message")
public class GenericData {

    @JacksonXmlProperty(localName = "DataSet", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message")
    public DataSet dataSet;
}


