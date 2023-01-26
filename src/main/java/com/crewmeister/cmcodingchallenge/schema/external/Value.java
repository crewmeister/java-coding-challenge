package com.crewmeister.cmcodingchallenge.schema.external;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Value {
    @JacksonXmlProperty(localName = "id")
    String id;

    @JacksonXmlProperty(localName = "value")
    String value;
}
