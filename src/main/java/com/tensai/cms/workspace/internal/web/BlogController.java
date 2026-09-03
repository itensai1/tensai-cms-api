package com.tensai.cms.workspace.internal.web;

import com.tensai.cms.shared.model.PageDto;
import com.tensai.cms.workspace.internal.service.BlogService;
import com.tensai.cms.workspace.internal.service.InteractionService;
import com.tensai.cms.workspace.internal.web.dto.BlogDto;
import com.tensai.cms.workspace.internal.web.dto.BlogInfo;
import com.tensai.cms.workspace.internal.web.dto.CommentDto;
import com.tensai.cms.workspace.internal.web.dto.ContentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final InteractionService interactionService;

    @GetMapping
    public ResponseEntity<PageDto<BlogInfo>> listBlogs(
            @PageableDefault(size = 20, sort = "updatedAt",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) UUID user
    ) {
        PageDto<BlogInfo> blogs = blogService.getBlogs(pageable, user);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogDto> getOne(
            @PathVariable UUID blogId
    ) {
        BlogDto blog = blogService.getBlogById(blogId);
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }

    @PostMapping("/{blogId}/likes")
    public ResponseEntity<Boolean> likeBlog(
            @PathVariable UUID blogId
    ) {
        boolean isLiked = interactionService.toggleLike(blogId);
        return new ResponseEntity<>(isLiked, HttpStatus.OK);
    }

    @GetMapping("/{blogId}/comments")
    public ResponseEntity<PageDto<CommentDto>> listBlogComments(
            @PageableDefault(size = 20, sort = "updatedAt",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable UUID blogId
    ) {
        PageDto<CommentDto> comments = interactionService.getBlogComments(pageable, blogId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/{blogId}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable UUID blogId,
            @Valid @RequestBody ContentRequest contentRequest
    ) {
        CommentDto newComment = interactionService.addComment(blogId, contentRequest.content());
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> editComment(
            @PathVariable UUID commentId,
            @Valid @RequestBody ContentRequest contentRequest
    ) {
        CommentDto editedComment = interactionService.editComment(commentId, contentRequest.content());
        return new ResponseEntity<>(editedComment, HttpStatus.OK);

    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable UUID commentId
    ) {
        interactionService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
