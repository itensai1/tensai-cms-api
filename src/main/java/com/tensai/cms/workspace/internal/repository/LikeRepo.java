package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface LikeRepo extends JpaRepository<Like, UUID> {
    @Query("""
            SELECT l.blog.id FROM Like l
            WHERE l.blog.id IN :blogIds
              AND l.userId = :userId
            """)
    Set<UUID> findLikedBlogIdsByUserId(Set<UUID> blogIds, UUID userId);

    Optional<Like> findByBlogIdAndUserId(UUID blogId, UUID userId);

    boolean existsByBlogIdAndUserId(UUID blogId, UUID userId);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.blog.id = :blogId")
    void deleteAllByBlogId(UUID blogId);
}