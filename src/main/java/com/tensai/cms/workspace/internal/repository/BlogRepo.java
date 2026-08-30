package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.Blog;
import com.tensai.cms.workspace.internal.repository.projection.BlogProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlogRepo extends JpaRepository<Blog, UUID> {
    Page<BlogProjection> findAllProjectedBy(Pageable pageable);

    Page<BlogProjection> findAllProjectedByUserId(UUID userId, Pageable pageable);

    boolean existsByUserId(UUID userId);
}
