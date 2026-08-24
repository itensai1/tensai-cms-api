package com.tensai.cms.telegram.internal.web.dto;

public enum EventType {
    REGISTER_USER,

    CREATE_TOPIC,
    UPDATE_TOPIC,

    CREATE_MESSAGE,
    UPDATE_MESSAGE,

    CALLBACK_QUERY;
}
