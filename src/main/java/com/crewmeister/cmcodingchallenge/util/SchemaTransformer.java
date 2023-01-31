package com.crewmeister.cmcodingchallenge.util;

import com.crewmeister.cmcodingchallenge.constants.Currency;
import com.crewmeister.cmcodingchallenge.schema.external.*;
import com.crewmeister.cmcodingchallenge.schema.internal.ConversionRate;
import com.crewmeister.cmcodingchallenge.schema.internal.CurrencyConversionRates;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemaTransformer {
    private static final String BBK_STD_CURRENCY = "BBK_STD_CURRENCY";

    public static CurrencyConversionRates transform(GenericData genericData) {
        var currencyConversionRates = new CurrencyConversionRates();
        var series = extractSeries(genericData);
        var obsList = extractObsList(series);
        if (!obsList.isEmpty()) {
            currencyConversionRates.setConversionRateList(
                    obsList.stream().map(e -> constructObject(series, e)).collect(Collectors.toList()));
        }
        return currencyConversionRates;
    }

    private static Series extractSeries(GenericData genericData) {
        return Optional.ofNullable(genericData)
                .map(o -> Optional.ofNullable(o.getDataSet()).orElse(new DataSet()))
                .map(o -> Optional.ofNullable(o.getSeries()).orElse(new Series())).get();
    }

    private static String extractSeriesValue(List<Value> valueList, String key) {
        return valueList.stream().filter(v -> key.equals(v.getId())).findFirst().orElse(new Value()).getValue();
    }

    private static List<Obs> extractObsList(Series series) {
        return Optional.ofNullable(series)
                .map(o -> Optional.ofNullable(o.getObsList()).orElse(new ArrayList<>()))
                .orElse(new ArrayList<>());
    }

    private static ConversionRate constructObject(Series series, Obs e) {
        return new ConversionRate(Double.parseDouble(
                Optional.ofNullable(e.getObsValue()).orElse(new ObsValue("0.0")).getValue()),
                Optional.ofNullable(e.getObsDimension()).orElse(new ObsDimension()).getValue(),
                Currency.valueOf(extractSeriesValue(series.getSeriesKey().getValueList(), BBK_STD_CURRENCY)));
    }
}
