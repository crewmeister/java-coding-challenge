package com.crewmeister.cmcodingchallenge.currency.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

public class XMLUtil {
    private static final XmlMapper xmlMapper = new XmlMapper();

    public static <T> T parseData(String xmlData, Class<T> typeC) throws IOException {
        return xmlMapper.readValue(xmlData, typeC);
    }
}
