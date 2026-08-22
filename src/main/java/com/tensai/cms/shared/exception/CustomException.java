package com.tensai.cms.shared.exception;

public class CustomException extends RuntimeException {
    private int code = 400;
    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }
    public CustomException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }
}