package com.tensai.cms.auth.internal.repository;

import com.tensai.cms.auth.internal.entity.User;
import com.tensai.cms.auth.internal.repository.projection.UserInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByTelegramUserId(Long telegramUserId);

    boolean existsByTelegramUserId(Long telegramUserId);

    @Query("SELECT u.id FROM User u WHERE u.telegramGroupId = :telegramGroupId")
    UUID findIdByTelegramGroupId(Long telegramGroupId);

    List<UserInfoProjection> findAllByIdIn(Set<UUID> ids);
}
