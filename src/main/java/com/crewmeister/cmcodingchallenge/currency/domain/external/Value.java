package com.crewmeister.cmcodingchallenge.currency.domain.external;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Value {
    @JacksonXmlProperty(localName = "id")
    String id;
    @JacksonXmlProperty(localName = "value")
    String value;
}
