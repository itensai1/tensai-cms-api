package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CommentRepo extends JpaRepository<Comment, UUID> {
    Page<Comment> findByBlogIdOrderByCreatedAtDesc(Pageable pageable, UUID blogId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.blog.id = :blogId")
    void deleteAllByBlogId(UUID blogId);
}
