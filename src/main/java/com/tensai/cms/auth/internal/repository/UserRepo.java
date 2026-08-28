package com.tensai.cms.auth.internal.repository;

import com.tensai.cms.auth.internal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByTelegramUserId(Long telegramUserId);

    @Query("SELECT u.id FROM User u WHERE u.telegramGroupId = :telegramGroupId")
    UUID findIdByTelegramGroupId(Long telegramGroupId);
}
