package com.crewmeister.cmcodingchallenge.xmldata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ObsDimension {
    @JacksonXmlProperty(isAttribute = true, localName = "value")
    public String date;
}
