package com.tensai.cms.workspace.internal.repository;

import com.tensai.cms.workspace.internal.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikeRepo extends JpaRepository<Like, UUID> {

}