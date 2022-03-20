package com.crewmeister.cmcodingchallenge.model;

public class Currency {

    private String unit;

    public Currency(String name) {
        this.unit = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
