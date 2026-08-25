package com.tensai.cms.telegram.api.commands;

public enum ButtonType {
    CALLBACK("callback_data"),
    URL("url"),
    COPY("copy_text");

    private final String field;

    ButtonType(String field) {
        this.field = field;
    }

    public String field() {
        return field;
    }
}
