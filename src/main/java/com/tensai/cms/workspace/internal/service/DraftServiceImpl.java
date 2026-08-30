package com.tensai.cms.workspace.internal.service;

import com.tensai.cms.auth.api.UserQueryService;
import com.tensai.cms.shared.exception.CustomException;
import com.tensai.cms.storage.api.StorageService;
import com.tensai.cms.telegram.api.TelegramService;
import com.tensai.cms.telegram.api.events.TelegramFile;
import com.tensai.cms.workspace.internal.entity.*;
import com.tensai.cms.workspace.internal.repository.BlogBlockRepo;
import com.tensai.cms.workspace.internal.repository.BlogRepo;
import com.tensai.cms.workspace.internal.repository.DraftRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DraftServiceImpl implements DraftService {
    private final DraftRepo draftRepo;
    private final UserQueryService userQueryService;
    private final StorageService storageService;
    private final TelegramService telegramService;
    private final BlogRepo blogRepo;
    private final BlogBlockRepo blogBlockRepo;

    @Override
    @Transactional
    public void CreateDraft(Long telegramGroupId, Long topicId, String title) {
        UUID userId = userQueryService.getUserIdByTelegramGroupId(telegramGroupId);

        Draft draft = new Draft(userId, topicId, title, null);
        draftRepo.save(draft);
    }

    @Override
    @Transactional
    public void updateDraftTitle(Long telegramGroupId, Long topicId, String title) {
        UUID userId = userQueryService.getUserIdByTelegramGroupId(telegramGroupId);
        Draft draft = draftRepo.findByUserIdAndTelegramTopicId(userId, topicId)
                .orElseThrow(() -> new CustomException(404, "No draft found with userId: %s and topicId: %s "
                        .formatted(userId, topicId)));
        draft.setTitle(title);
        draft.setSynced(false);
        draftRepo.save(draft);
    }

    @Override
    @Transactional
    public void addDraftBlock(Long telegramGroupId, Long topicId, int position, String text, TelegramFile file) throws IOException {
        if (text == null && file == null) return;

        BlockType type = file == null ? BlockType.TEXT : BlockType.valueOf(file.type());

        UUID userId = userQueryService.getUserIdByTelegramGroupId(telegramGroupId);
        Draft draft = draftRepo.findByUserIdAndTelegramTopicId(userId, topicId)
                .orElseThrow(() -> new CustomException(404, "No draft found with userId: %s and topicId: %s "
                        .formatted(userId, topicId)));

        String mediaUrl = null;

        if (file != null) {
            Resource resource = telegramService.getFile(file.fileId());
            String mimeType = file.mimeType() != null ? file.mimeType() : getResourceMimeType(resource);
            mediaUrl = storageService.store(resource, mimeType);
        }

        DraftBlock block = new DraftBlock(draft, position, type, text, mediaUrl);
        draft.addBlock(block);
        draft.setSynced(false);
        draftRepo.save(draft);
    }

    @Override
    @Transactional
    public void deleteDraftBlock(Long telegramGroupId, Long topicId, int position) {
        UUID userId = userQueryService.getUserIdByTelegramGroupId(telegramGroupId);
        Draft draft = draftRepo.findByUserIdAndTelegramTopicId(userId, topicId)
                .orElseThrow(() -> new CustomException(404, "No draft found with userId: %s and topicId: %s "
                        .formatted(userId, topicId)));
        draft.removeBlockWithPosition(position);
        draft.setSynced(false);
        draftRepo.save(draft);
    }

    @Override
    @Transactional
    public void updateDraftBlock(Long telegramGroupId, Long topicId, int position, String text, TelegramFile file) throws IOException {
        UUID userId = userQueryService.getUserIdByTelegramGroupId(telegramGroupId);
        Draft draft = draftRepo.findByUserIdAndTelegramTopicId(userId, topicId)
                .orElseThrow(() -> new CustomException(404, "No draft found with userId: %s and topicId: %s "
                        .formatted(userId, topicId)));

        if (text == null && file == null) {
            deleteDraftBlock(telegramGroupId, topicId, position);
            return;
        }

        BlockType type = file == null ? BlockType.TEXT : BlockType.valueOf(file.type());
        String newMediaUrl;

        if (file != null) {
            Resource resource = telegramService.getFile(file.fileId());
            String mimeType = file.mimeType() != null ? file.mimeType() : getResourceMimeType(resource);
            newMediaUrl = storageService.store(resource, mimeType);
        } else {
            newMediaUrl = null;
        }

        draft.getBlocks().stream().filter(b -> b.getPosition() == position)
                .findFirst().ifPresent(block -> {
                    block.setText(text);
                    block.setMediaUrl(newMediaUrl);
                    block.setType(type);
                });
        draft.setSynced(false);
        draftRepo.save(draft);
    }

    @Override
    @Transactional
    public void updateDraftSummary(Long telegramGroupId, Long topicId, String summary) {
        UUID userId = userQueryService.getUserIdByTelegramGroupId(telegramGroupId);
        Draft draft = draftRepo.findByUserIdAndTelegramTopicId(userId, topicId)
                .orElseThrow(() -> new CustomException(404, "No draft found with userId: %s and topicId: %s "
                        .formatted(userId, topicId)));
        draft.setSummary(summary);
        draft.setSynced(false);
        draftRepo.save(draft);
    }

    @Override
    @Transactional
    public void deleteDraftSummary(Long telegramGroupId, Long topicId) {
        updateDraftSummary(telegramGroupId, topicId, null);
    }

    @Override
    @Transactional
    public void publishDraft(Long telegramGroupId, Long topicId) {
        UUID userId = userQueryService.getUserIdByTelegramGroupId(telegramGroupId);

        if (draftRepo.existsByUserIdAndTelegramTopicIdAndSyncedTrue(userId, topicId)) return;

        Draft draft = draftRepo.findByUserIdAndTelegramTopicId(userId, topicId)
                .orElseThrow(() -> new CustomException(404, "No draft found with userId: %s and topicId: %s "
                        .formatted(userId, topicId)));


        Blog blog = draft.getBlog();
        if (blog == null) { // new blog
            blog = new Blog(userId, draft.getTitle(), draft.getSummary());
            blogRepo.save(blog);
            draft.setBlog(blog);
        } else { // old blog
            blog.setTitle(draft.getTitle());
            blog.setSummary(draft.getSummary());
        }

        // delete old BlogBlocks
        blogBlockRepo.deleteAllByBlogId(blog.getId());
        blog.getBlocks().clear();

        // copy dtaft blocks to blog
        for (DraftBlock block : draft.getBlocks()) {
            blog.addBlock(
                    new BlogBlock(
                            blog,
                            block.getPosition(),
                            block.getType(),
                            block.getText(),
                            block.getMediaUrl()
                    )
            );
        }

        draft.setSynced(true);
        blogRepo.save(blog);
        draftRepo.save(draft);
    }

    private String getResourceMimeType(Resource resource) {
        return MediaTypeFactory.getMediaType(resource)
                .map(MediaType::toString)
                .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }
}
