package com.crewmeister.cmcodingchallenge.schema.external;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "GenericData")
public class GenericData {
    String schemaLocation;

    @JacksonXmlProperty(localName ="Header")
    Header header;

    @JacksonXmlProperty(localName ="DataSet")
    DataSet dataSet;
}
