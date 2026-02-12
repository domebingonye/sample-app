package com.sbsc_fcmb.sample_app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResponseCodes {
    SUCCESS("00", "Success"),
    FAILURE("01", "Failure"),
    BAD_REQUEST("400", "Bad request"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
    UNAUTHORIZED("401", "Unauthorized"),
    NOT_FOUND("404", "Not found");

    @Getter
    private final String code;
    @Getter
    private final String message;
}
