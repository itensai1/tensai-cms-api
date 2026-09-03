package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepo extends JpaRepository<Comment, UUID> {

}
