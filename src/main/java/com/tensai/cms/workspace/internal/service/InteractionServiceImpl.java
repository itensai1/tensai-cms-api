package com.tensai.cms.workspace.internal.service;

import com.tensai.cms.auth.api.UserInfo;
import com.tensai.cms.auth.api.UserQueryService;
import com.tensai.cms.shared.exception.CustomException;
import com.tensai.cms.shared.model.PageDto;
import com.tensai.cms.workspace.internal.entity.Blog;
import com.tensai.cms.workspace.internal.entity.Comment;
import com.tensai.cms.workspace.internal.entity.Like;
import com.tensai.cms.workspace.internal.repository.BlogRepo;
import com.tensai.cms.workspace.internal.repository.CommentRepo;
import com.tensai.cms.workspace.internal.repository.LikeRepo;
import com.tensai.cms.workspace.internal.web.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {
    private final LikeRepo likeRepo;
    private final CommentRepo commentRepo;
    private final BlogRepo blogRepo;
    private final UserQueryService userQueryService;

    @Override
    @Transactional
    public boolean toggleLike(UUID blogId) {
        UUID currentUserId = userQueryService.getCurrentUserId();
        Optional<Like> like = likeRepo.findByBlogIdAndUserId(blogId, currentUserId);

        if (like.isPresent()) {
            likeRepo.delete(like.get());
            blogRepo.decrementLikesCountById(blogId);
            return false;
        } else {
            Blog blog = blogRepo.findById(blogId).orElseThrow(() -> new CustomException(404, "Blog with id {%s} not found".formatted(blogId)));
            likeRepo.save(new Like(blog, currentUserId));
            blogRepo.incrementLikesCountById(blogId);
            return true;
        }
    }

    @Override
    @Transactional
    public CommentDto addComment(UUID blogId, String content) {
        Blog blog = blogRepo.findById(blogId).orElseThrow(() -> new CustomException(404, "Blog with id {%s} not found".formatted(blogId)));
        UserInfo currentUserInfo = userQueryService.getCurrentUserInfo();
        Comment comment = new Comment(blog, currentUserInfo.id(), content.strip());
        commentRepo.save(comment);
        blogRepo.incrementCommentsCountById(blogId);

        return CommentDto.builder()
                .id(comment.getId()).userId(comment.getUserId())
                .content(comment.getContent()).lastUpdated(comment.getUpdatedAt())
                .isEdited(comment.isEdited())
                .username(currentUserInfo.username())
                .commenterName(currentUserInfo.fullName())
                .isAuthor(blog.getUserId().equals(currentUserInfo.id())).build();
    }

    @Override
    @Transactional
    @PreAuthorize("@interactionServiceImpl.isCommentOwner(#commentId)")
    public CommentDto editComment(UUID commentId, String content) {
        UserInfo currentUserInfo = userQueryService.getCurrentUserInfo();
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CustomException(404, "Comment with id {%s} not found".formatted(commentId)));
        comment.setContent(content.strip());
        comment.setEdited(true);
        commentRepo.save(comment);
        return CommentDto.builder()
                .id(commentId).userId(comment.getUserId())
                .content(content).lastUpdated(comment.getUpdatedAt())
                .isEdited(comment.isEdited()).username(currentUserInfo.username())
                .commenterName(currentUserInfo.fullName())
                .isAuthor(comment.getBlog().getUserId().equals(currentUserInfo.id())).build();
    }

    @Override
    @Transactional
    @PreAuthorize("@interactionServiceImpl.isCommentOwnerOrAuthor(#commentId)")
    public void deleteComment(UUID commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CustomException(404, "Comment with id {%s} not found".formatted(commentId)));
        UUID blogId = comment.getBlog().getId();
        commentRepo.deleteById(commentId);
        blogRepo.decrementCommentsCountById(blogId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<CommentDto> getBlogComments(Pageable pageable, UUID blogId) {
        Page<Comment> comments = commentRepo.findByBlogIdOrderByCreatedAtDesc(pageable, blogId);

        Set<UUID> userIds = comments.getContent().stream()
                .map(Comment::getUserId).collect(Collectors.toSet());

        Map<UUID, UserInfo> userInfoMap = userQueryService.getUserInfoByIds(userIds);

        List<CommentDto> items = comments.getContent().stream()
                .map(c -> CommentDto.builder()
                        .id(c.getId()).userId(c.getUserId())
                        .content(c.getContent()).lastUpdated(c.getUpdatedAt())
                        .isEdited(c.isEdited())
                        .username(userInfoMap.get(c.getUserId()).username())
                        .commenterName(userInfoMap.get(c.getUserId()).fullName())
                        .isAuthor(c.getUserId().equals(c.getBlog().getUserId())).build()
                ).toList();

        return PageDto.<CommentDto>builder()
                .content(items)
                .currentPage(pageable.getPageNumber())
                .totalPages(comments.getTotalPages())
                .totalItems(comments.getTotalElements())
                .hasNext(comments.hasNext())
                .hasPrevious(comments.hasPrevious()).build();
    }

    @Override
    public boolean isCommentOwner(UUID commentId) {
        Comment comment = commentRepo.findById(commentId).orElse(null);
        UserInfo info = userQueryService.getCurrentUserInfo();
        if (comment == null || info == null) return false;
        return comment.getUserId().equals(info.id());
    }

    @Override
    public boolean isCommentOwnerOrAuthor(UUID commentId) {
        Comment comment = commentRepo.findById(commentId).orElse(null);
        UserInfo info = userQueryService.getCurrentUserInfo();
        if (comment == null || info == null) return false;
        if (comment.getUserId().equals(info.id())) return true;
        return comment.getBlog().getUserId().equals(info.id());
    }

}
