package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.BlogBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface BlogBlockRepo extends JpaRepository<BlogBlock, UUID> {
    @Modifying
    @Query("delete from BlogBlock bb where bb.blog.id = :blogId")
    void deleteAllByBlogId(UUID blogId);
}
