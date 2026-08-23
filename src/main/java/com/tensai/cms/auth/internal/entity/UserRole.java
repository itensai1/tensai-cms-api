package com.tensai.cms.auth.internal.entity;

public enum UserRole {
    USER,
    ADMIN;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
