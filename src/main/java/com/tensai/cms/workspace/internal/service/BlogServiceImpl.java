package com.tensai.cms.workspace.internal.service;

import com.tensai.cms.auth.api.UserInfo;
import com.tensai.cms.auth.api.UserQueryService;
import com.tensai.cms.shared.model.PageDto;
import com.tensai.cms.shared.exception.CustomException;
import com.tensai.cms.workspace.internal.entity.Blog;
import com.tensai.cms.workspace.internal.repository.BlogRepo;
import com.tensai.cms.workspace.internal.repository.projection.BlogProjection;
import com.tensai.cms.workspace.internal.web.dto.BlogBlockDto;
import com.tensai.cms.workspace.internal.web.dto.BlogDto;
import com.tensai.cms.workspace.internal.web.dto.BlogInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepo blogRepo;
    private final UserQueryService userQueryService;

    @Override
    @Transactional(readOnly = true)
    public PageDto<BlogInfo> getBlogs(Pageable pageable, UUID userId) {
        Page<BlogProjection> blogs;

        if (userId == null) {
            blogs = blogRepo.findAllProjectedBy(pageable);
        } else {
            if (blogRepo.existsByUserId(userId)) {
                blogs = blogRepo.findAllProjectedByUserId(userId, pageable);
            } else {
                return new PageDto<>(List.of(), pageable.getPageNumber(), 0, 0, false, false);
            }
        }

        Set<UUID> userIds = blogs.getContent().stream()
                .map(BlogProjection::getUserId)
                .collect(Collectors.toSet());

        Map<UUID, UserInfo> userInfoMap = userQueryService.getUserInfoByIds(userIds);

        List<BlogInfo> items = blogs.getContent().stream()
                .map(b -> BlogInfo.builder()
                        .id(b.getId()).title(b.getTitle())
                        .summary(b.getSummary()).lastUpdated(b.getUpdatedAt())
                        .userId(b.getUserId())
                        .username(userInfoMap.get(b.getUserId()).username())
                        .publisherName(userInfoMap.get(b.getUserId()).fullName()).build()
                ).toList();

        return PageDto.<BlogInfo>builder()
                .content(items)
                .currentPage(pageable.getPageNumber())
                .totalPages(blogs.getTotalPages())
                .totalItems(blogs.getTotalElements())
                .hasNext(blogs.hasNext())
                .hasPrevious(blogs.hasPrevious()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public BlogDto getBlogById(UUID id) {
        Blog blog = blogRepo.findById(id).orElseThrow(
                () -> new CustomException(404, "Blog with id: " + id + " isn't found")
        );

        UserInfo userInfo = userQueryService.getUserInfoByIds(Set.of(blog.getUserId())).get(blog.getUserId());

        List<BlogBlockDto> blocks = blog.getBlocks().stream()
                .map(b -> BlogBlockDto.builder()
                        .id(b.getId()).position(b.getPosition())
                        .type(b.getType()).text(b.getText())
                        .mediaUrl(b.getMediaUrl()).lastUpdated(b.getUpdatedAt())
                        .build()
                ).toList();

        return BlogDto.builder()
                .id(blog.getId()).title(blog.getTitle())
                .summary(blog.getSummary()).lastUpdated(blog.getUpdatedAt())
                .userId(blog.getUserId()).blocks(blocks)
                .username(userInfo.username())
                .publisherName(userInfo.fullName()).build();
    }

}
