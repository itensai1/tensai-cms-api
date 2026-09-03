package com.tensai.cms.workspace.internal.service;

import com.tensai.cms.shared.model.PageDto;
import com.tensai.cms.workspace.internal.web.dto.CommentDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InteractionService {
    boolean toggleLike(UUID blogId);

    CommentDto addComment(UUID blogId, String content);

    CommentDto editComment(UUID commentId, String content);

    void deleteComment(UUID commentId);

    PageDto<CommentDto> getBlogComments(Pageable pageable, UUID blogId);

    boolean isCommentOwner(UUID commentId);

    boolean isCommentOwnerOrAuthor(UUID commentId);
}
