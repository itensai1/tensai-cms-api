package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlogRepo extends JpaRepository<Blog, UUID> {
}
