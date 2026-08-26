CREATE TABLE drafts
(
    id                UUID         NOT NULL,

    user_id           UUID         NOT NULL,

    blog_id           UUID,

    telegram_topic_id BIGINT       NOT NULL,

    title             VARCHAR(255) NOT NULL,

    summary           TEXT,

    is_synced         BOOLEAN      NOT NULL DEFAULT TRUE,

    created_at        TIMESTAMPTZ  NOT NULL,

    updated_at        TIMESTAMPTZ  NOT NULL,

    CONSTRAINT pk_drafts
        PRIMARY KEY (id),

    CONSTRAINT uk_drafts_blog_id
        UNIQUE (blog_id),

    CONSTRAINT uk_drafts_telegram_topic_id
        UNIQUE (telegram_topic_id),

    CONSTRAINT fk_drafts_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT fk_drafts_blog
        FOREIGN KEY (blog_id)
            REFERENCES blogs (id)
);


CREATE TABLE draft_blocks
(
    id         UUID        NOT NULL,

    draft_id   UUID        NOT NULL,

    position   INTEGER     NOT NULL,

    type       VARCHAR(20) NOT NULL,

    text       TEXT,

    media_url  VARCHAR(255),

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_draft_blocks
        PRIMARY KEY (id),

    CONSTRAINT fk_draft_blocks_draft
        FOREIGN KEY (draft_id)
            REFERENCES drafts (id),

    CONSTRAINT uk_draft_blocks_draft_position
        UNIQUE (draft_id, position),

    CONSTRAINT chk_draft_blocks_content
        CHECK (
            text IS NOT NULL
                OR media_url IS NOT NULL
            )
);