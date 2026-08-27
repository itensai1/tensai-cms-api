package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.BlogBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlogBlockRepo extends JpaRepository<BlogBlock, UUID> {

}
