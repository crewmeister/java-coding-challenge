package com.crewmeister.cmcodingchallenge.schema.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ObsValue {
    private String value;

    public ObsValue() {
    }
}
