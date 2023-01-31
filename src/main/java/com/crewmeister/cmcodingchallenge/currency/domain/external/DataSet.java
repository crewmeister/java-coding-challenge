package com.crewmeister.cmcodingchallenge.currency.domain.external;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@Jacksonized
public class DataSet {
    String structureRef;
    String setID;
    String action;
    String validFromDate;

    @JacksonXmlProperty(localName ="Series")
    Series series;
}
