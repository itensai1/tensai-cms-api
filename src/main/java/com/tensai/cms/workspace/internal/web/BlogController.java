package com.tensai.cms.workspace.internal.web;

import com.tensai.cms.shared.model.PageDto;
import com.tensai.cms.workspace.internal.service.BlogService;
import com.tensai.cms.workspace.internal.web.dto.BlogDto;
import com.tensai.cms.workspace.internal.web.dto.BlogInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<PageDto<BlogInfo>> listBlogs(
            @PageableDefault(size = 20, sort = "updatedAt",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) UUID user
    ) {
        PageDto<BlogInfo> blogs = blogService.getBlogs(pageable, user);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDto> getOne(
            @PathVariable UUID id
    ) {
        BlogDto blog = blogService.getBlogById(id);
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }
}
