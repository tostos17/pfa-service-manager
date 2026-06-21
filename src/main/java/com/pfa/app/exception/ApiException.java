package com.pfa.app.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }
}
