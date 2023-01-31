package com.crewmeister.cmcodingchallenge.currency.domain.external;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@JacksonXmlRootElement(localName = "GenericData")
@Jacksonized
public class GenericData {
    String schemaLocation;

    @JacksonXmlProperty(localName ="Header")
    Header header;

    @JacksonXmlProperty(localName ="DataSet")
    DataSet dataSet;
}
