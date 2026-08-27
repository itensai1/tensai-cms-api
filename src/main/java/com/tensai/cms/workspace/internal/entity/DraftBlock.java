package com.tensai.cms.workspace.internal.entity;

import com.tensai.cms.shared.exception.CustomException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(
        name = "draft_blocks",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_draft_blocks_draft_position",
                columnNames = {"draft_id", "position"}
        )
)
public class DraftBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "draft_id", nullable = false)
    private Draft draft;

    @Column(name = "position", nullable = false)
    private int position;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private BlockType type;

    @Column(name = "text", columnDefinition = "text")
    private String text;

    @Column(name = "media_url", length = 255)
    private String mediaUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public DraftBlock(Draft draft, int position, BlockType type, String text, String mediaUrl) {
        this.draft = draft;
        this.position = position;
        this.type = type;
        this.text = text;
        this.mediaUrl = mediaUrl;
        validateContent();
    }

    public DraftBlock() {
    }

    public void validateContent() {
        if ((text == null || text.isBlank()) && (mediaUrl == null || mediaUrl.isBlank())) {
            throw new CustomException(
                    "A draft block must have either text or a media");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id != null && id.equals(((DraftBlock) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}