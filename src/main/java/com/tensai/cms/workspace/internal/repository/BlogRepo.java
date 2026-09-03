package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.Blog;
import com.tensai.cms.workspace.internal.repository.projection.BlogProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface BlogRepo extends JpaRepository<Blog, UUID> {
    Page<BlogProjection> findAllProjectedBy(Pageable pageable);

    Page<BlogProjection> findAllProjectedByUserId(UUID userId, Pageable pageable);

    boolean existsByUserId(UUID userId);

    @Modifying
    @Query("UPDATE Blog b SET b.likesCount = b.likesCount + 1 WHERE b.id = :id")
    int incrementLikesCountById(UUID id);

    @Modifying
    @Query("UPDATE Blog b SET b.likesCount = b.likesCount - 1 WHERE b.id = :id AND b.likesCount > 0")
    int decrementLikesCountById(UUID id);

    @Modifying
    @Query("UPDATE Blog b SET b.commentsCount = b.commentsCount + 1 WHERE b.id = :id")
    int incrementCommentsCountById(UUID id);

    @Modifying
    @Query("UPDATE Blog b SET b.commentsCount = b.commentsCount - 1 WHERE b.id = :id AND b.commentsCount > 0")
    int decrementCommentsCountById(UUID id);
}
