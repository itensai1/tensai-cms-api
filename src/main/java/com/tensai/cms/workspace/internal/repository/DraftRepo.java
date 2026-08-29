package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.Draft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DraftRepo extends JpaRepository<Draft, UUID> {
    Optional<Draft> findByUserIdAndTelegramTopicId(UUID userId, Long telegramTopicId);
}
