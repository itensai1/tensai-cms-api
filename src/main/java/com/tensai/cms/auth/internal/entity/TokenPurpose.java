package com.tensai.cms.auth.internal.entity;

import java.util.Map;

public enum TokenPurpose {
    LOGIN;

    public Map<String, Object> purposeClaim() {
        return Map.of("purpose", this.name());
    }
}
