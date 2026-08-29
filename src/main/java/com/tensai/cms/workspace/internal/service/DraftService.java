package com.tensai.cms.workspace.internal.service;

import com.tensai.cms.telegram.api.events.TelegramFile;

import java.io.IOException;

public interface DraftService {
    void CreateDraft(Long telegramGroupId, Long topicId, String title);

    void updateDraftTitle(Long telegramGroupId, Long topicId, String title);

    void addDraftBlock(Long telegramGroupId, Long topicId, int position, String text, TelegramFile file) throws IOException;

    void deleteDraftBlock(Long telegramGroupId, Long topicId, int position);

    void updateDraftBlock(Long telegramGroupId, Long topicId, int position, String text, TelegramFile file) throws IOException;

    void updateDraftSummary(Long telegramGroupId, Long topicId, String summary);

    void deleteDraftSummary(Long telegramGroupId, Long topicId);

}
