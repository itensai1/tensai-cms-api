package com.tensai.cms.workspace.internal.service;

import com.tensai.cms.shared.dto.PageDto;
import com.tensai.cms.workspace.internal.web.dto.BlogDto;
import com.tensai.cms.workspace.internal.web.dto.BlogInfo;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BlogService {
    PageDto<BlogInfo> getBlogs(Pageable pageable, UUID userId);

    BlogDto getBlogById(UUID id);
}
