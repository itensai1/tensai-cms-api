package com.tensai.cms.telegram.api.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageEntity(
        Integer offset,
        Integer length,
        String type
) {
}
