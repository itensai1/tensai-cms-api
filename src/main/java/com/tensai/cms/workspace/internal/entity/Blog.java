package com.tensai.cms.workspace.internal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "summary", columnDefinition = "text")
    private String summary;

    @OneToMany(
            mappedBy = "blog",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("position ASC")
    private Set<BlogBlock> blocks = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Blog(UUID userId, String title, String summary) {
        this.userId = userId;
        this.title = title;
        this.summary = summary;
    }

    public Blog() {
    }

    public void addBlock(BlogBlock block) {
        blocks.add(block);
        block.setBlog(this);
    }

    public void removeBlock(BlogBlock block) {
        blocks.remove(block);
        block.setBlog(null);
    }
}
