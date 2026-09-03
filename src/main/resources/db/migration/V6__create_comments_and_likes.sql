-- Add to blogs
ALTER TABLE blogs
    ADD COLUMN comments_count INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN likes_count    INTEGER NOT NULL DEFAULT 0;


CREATE TABLE comments
(
    id         UUID        NOT NULL,
    blog_id    UUID        NOT NULL,
    user_id    UUID        NOT NULL,
    content    TEXT        NOT NULL,
    is_edited  BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_comments
        PRIMARY KEY (id),

    CONSTRAINT fk_comments_blog
        FOREIGN KEY (blog_id)
            REFERENCES blogs (id),

    CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

CREATE INDEX idx_comments_blog_id
    ON comments (blog_id);

CREATE INDEX idx_comments_user_id
    ON comments (user_id);


CREATE TABLE likes
(
    id         UUID        NOT NULL,
    blog_id    UUID        NOT NULL,
    user_id    UUID        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_likes
        PRIMARY KEY (id),

    CONSTRAINT fk_likes_blog
        FOREIGN KEY (blog_id)
            REFERENCES blogs (id),

    CONSTRAINT fk_likes_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT uk_likes_blog_user
        UNIQUE (blog_id, user_id)
);

CREATE INDEX idx_likes_blog_id
    ON likes (blog_id);