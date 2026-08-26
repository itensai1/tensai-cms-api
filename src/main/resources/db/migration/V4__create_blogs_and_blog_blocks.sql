CREATE TABLE blogs
(
    id         UUID         NOT NULL,

    user_id    UUID         NOT NULL,

    title      VARCHAR(255) NOT NULL,

    summary    TEXT,

    created_at TIMESTAMPTZ  NOT NULL,

    updated_at TIMESTAMPTZ  NOT NULL,

    CONSTRAINT pk_blogs
        PRIMARY KEY (id),

    CONSTRAINT fk_blogs_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

CREATE INDEX idx_blogs_user_id
    ON blogs (user_id);

CREATE TABLE blog_blocks
(
    id         UUID        NOT NULL,

    blog_id    UUID        NOT NULL,

    position   INTEGER     NOT NULL,

    type       VARCHAR(20) NOT NULL,

    text       TEXT,

    media_url  VARCHAR(255),

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_blog_blocks
        PRIMARY KEY (id),

    CONSTRAINT fk_blog_blocks_blog
        FOREIGN KEY (blog_id)
            REFERENCES blogs (id),

    CONSTRAINT uk_blog_blocks_blog_position
        UNIQUE (blog_id, position),

    CONSTRAINT chk_blog_blocks_content
        CHECK (
            text IS NOT NULL
                OR media_url IS NOT NULL
            )
);