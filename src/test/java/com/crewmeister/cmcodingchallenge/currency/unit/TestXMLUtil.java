package com.crewmeister.cmcodingchallenge.currency.unit;

import com.crewmeister.cmcodingchallenge.currency.domain.external.GenericData;
import com.crewmeister.cmcodingchallenge.currency.util.XMLUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestXMLUtil {

    @Test
    void testXmlToEntity() throws IOException {
        String resourceName = "test-data.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        String content = Files.readString(file.toPath());
        GenericData value = XMLUtil.parseData(content, GenericData.class);
        System.out.println(value.getDataSet());
        assertNotNull(value);
        assertNotNull(value.getDataSet());
        assertNotNull(value.getDataSet().getSeries());
        assertNotNull(value.getDataSet().getSeries().getObsList());
        assertEquals(10, value.getDataSet().getSeries().getObsList().size());
        assertNotNull(value.getDataSet().getSeries().getObsList().get(0).getObsValue());
        assertNotNull(value.getDataSet().getSeries().getObsList().get(0).getObsValue().getValue());
    }
}
