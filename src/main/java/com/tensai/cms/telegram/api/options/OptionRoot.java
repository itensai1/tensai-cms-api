package com.tensai.cms.telegram.api.options;

public enum OptionRoot {
    AUTH("auth"),
    WORKSPACE("ws");

    private final String value;

    OptionRoot(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static OptionRoot fromValue(String value) {
        for (OptionRoot o : values()) {
            if (o.value().equals(value)) {
                return o;
            }
        }
        return null;
    }

}
