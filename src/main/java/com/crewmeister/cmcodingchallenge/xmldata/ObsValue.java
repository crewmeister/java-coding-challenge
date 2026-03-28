package com.crewmeister.cmcodingchallenge.xmldata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ObsValue {
    @JacksonXmlProperty(isAttribute = true, localName = "value")
    public String rate;
}
