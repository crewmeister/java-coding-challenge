package com.crewmeister.cmcodingchallenge.exchangerate.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response having a standard response-message + data structure
 *
 * @author jeevan
 */
@Getter
@JsonInclude(NON_NULL)
@AllArgsConstructor(staticName = "build")
public class Response {
    private final String message;
    private final Object data;

    /**
     * @param data
     * @return
     */
    public static Response success(final Object data) {
        return build("ok", data);
    }

    /**
     * @param data
     * @return
     */
    public static Response error(final String data) {
        return build("error", data);
    }

    /**
     * @param ex
     * @return
     */
    public static Response error(Throwable ex) {
        return error(ex.toString());
    }
}
