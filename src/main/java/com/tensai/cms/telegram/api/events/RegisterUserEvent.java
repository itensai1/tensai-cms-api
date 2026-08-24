package com.tensai.cms.telegram.api.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterUserEvent(
        @JsonProperty("telegram_user_id")
        Long telegramUserId,

        String username,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("telegram_group_id")
        Long telegramGroupId
) {
}
