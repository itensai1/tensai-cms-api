package com.tensai.cms.workspace.internal.web;

import com.tensai.cms.shared.model.PageDto;
import com.tensai.cms.workspace.internal.service.BlogService;
import com.tensai.cms.workspace.internal.service.InteractionService;
import com.tensai.cms.workspace.internal.web.dto.BlogDto;
import com.tensai.cms.workspace.internal.web.dto.BlogInfo;
import com.tensai.cms.workspace.internal.web.dto.CommentDto;
import com.tensai.cms.workspace.internal.web.dto.ContentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Blogs", description = "Endpoints for managing and interacting with blog posts and comments")
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final InteractionService interactionService;

    @Operation(
            summary = "List blog posts",
            description = "Retrieves a paginated list of blog previews, optionally filtered by user ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved blog list")
    })
    @SecurityRequirements // Marks endpoint as public in Swagger UI
    @GetMapping
    public ResponseEntity<PageDto<BlogInfo>> listBlogs(
            @ParameterObject
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,

            @Parameter(description = "Optional filter by author/user UUID")
            @RequestParam(required = false) UUID user
    ) {
        PageDto<BlogInfo> blogs = blogService.getBlogs(pageable, user);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @Operation(
            summary = "Get blog post by ID",
            description = "Fetches a single blog post along with its structured content blocks."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Blog post not found")
    })
    @SecurityRequirements // Marks endpoint as public in Swagger UI
    @GetMapping("/{blogId}")
    public ResponseEntity<BlogDto> getOne(
            @Parameter(description = "UUID of the blog post", required = true)
            @PathVariable UUID blogId
    ) {
        BlogDto blog = blogService.getBlogById(blogId);
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }

    @Operation(
            summary = "Toggle like status",
            description = "Toggles the like state on a blog post for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Toggle operation successful (returns true if currently liked, false if unliked)"),
            @ApiResponse(responseCode = "404", description = "Blog post not found")
    })
    @PostMapping("/{blogId}/likes")
    public ResponseEntity<Boolean> likeBlog(
            @Parameter(description = "UUID of the blog post", required = true)
            @PathVariable UUID blogId
    ) {
        boolean isLiked = interactionService.toggleLike(blogId);
        return new ResponseEntity<>(isLiked, HttpStatus.OK);
    }

    @Operation(
            summary = "List blog comments",
            description = "Retrieves a paginated list of comments associated with a specific blog post."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
            @ApiResponse(responseCode = "404", description = "Blog post not found")
    })
    @GetMapping("/{blogId}/comments")
    public ResponseEntity<PageDto<CommentDto>> listBlogComments(
            @ParameterObject
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,

            @Parameter(description = "UUID of the blog post", required = true)
            @PathVariable UUID blogId
    ) {
        PageDto<CommentDto> comments = interactionService.getBlogComments(pageable, blogId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Operation(
            summary = "Add a comment",
            description = "Posts a new comment to an existing blog post."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid comment request body"),
            @ApiResponse(responseCode = "404", description = "Blog post not found")
    })
    @PostMapping("/{blogId}/comments")
    public ResponseEntity<CommentDto> addComment(
            @Parameter(description = "UUID of the blog post", required = true)
            @PathVariable UUID blogId,

            @Valid @RequestBody ContentRequest contentRequest
    ) {
        CommentDto newComment = interactionService.addComment(blogId, contentRequest.content());
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Edit a comment",
            description = "Updates the content of an existing comment by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid comment request body"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> editComment(
            @Parameter(description = "UUID of the comment", required = true)
            @PathVariable UUID commentId,

            @Valid @RequestBody ContentRequest contentRequest
    ) {
        CommentDto editedComment = interactionService.editComment(commentId, contentRequest.content());
        return new ResponseEntity<>(editedComment, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a comment",
            description = "Removes a comment from a blog post by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "UUID of the comment to delete", required = true)
            @PathVariable UUID commentId
    ) {
        interactionService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
