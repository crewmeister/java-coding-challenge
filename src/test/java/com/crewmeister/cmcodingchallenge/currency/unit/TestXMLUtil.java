package com.crewmeister.cmcodingchallenge.currency.unit;

import com.crewmeister.cmcodingchallenge.currency.util.XMLUtil;
import com.crewmeister.cmcodingchallenge.currency.domain.external.GenericData;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestXMLUtil {

    @Test
    void testXmlToEntity() throws IOException {
        String resourceName = "test-data.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        String content = Files.readString(file.toPath());
        GenericData value = XMLUtil.parseData(content, GenericData.class);
        System.out.println(value.getDataSet());
    }
}