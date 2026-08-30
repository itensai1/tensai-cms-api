package com.tensai.cms.workspace.internal.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Entity
@Table(
        name = "drafts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_drafts_blog_id", columnNames = "blog_id"),
                @UniqueConstraint(name = "uk_drafts_telegram_topic_id",
                        columnNames = "telegram_topic_id")
        }
)
public class Draft {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Column(name = "telegram_topic_id", nullable = false, unique = true)
    private Long telegramTopicId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "summary", columnDefinition = "text")
    private String summary;

    @Column(name = "is_synced", nullable = false)
    private boolean synced = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "draft",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderBy("position ASC")
    private Set<DraftBlock> blocks = new HashSet<>();

    public Draft(UUID userId, Long telegramTopicId, String title, String summary) {
        this.userId = userId;
        this.telegramTopicId = telegramTopicId;
        this.title = title;
        this.summary = summary;
    }

    protected Draft() {
    }

    public void addBlock(DraftBlock block) {
        blocks.add(block);
        block.setDraft(this);
    }

    public void removeBlock(DraftBlock block) {
        blocks.remove(block);
        block.setDraft(null);
    }

    public void removeBlockWithPosition(int position) {
        blocks.stream()
                .filter(b -> b.getPosition() == position)
                .findFirst()
                .ifPresent(block -> {
                    blocks.remove(block);
                    block.setDraft(null);
                });
    }
}
