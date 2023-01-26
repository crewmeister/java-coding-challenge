package com.crewmeister.cmcodingchallenge.schema.external;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class DataSet {
    String structureRef;
    String setID;
    String action;
    String validFromDate;

    @JacksonXmlProperty(localName ="Series")
    Series series;
}
