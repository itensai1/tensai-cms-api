CREATE TABLE users
(
    id                UUID         NOT NULL,

    telegram_user_id  BIGINT       NOT NULL,

    telegram_group_id BIGINT       NOT NULL,

    username          VARCHAR(255) NOT NULL,

    password          VARCHAR(255) NOT NULL,

    role              VARCHAR(20)  NOT NULL,

    first_name        VARCHAR(255) NOT NULL,

    last_name         VARCHAR(255),

    photo_url         VARCHAR(255),

    is_admin_bot      BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at        TIMESTAMPTZ  NOT NULL,

    updated_at        TIMESTAMPTZ  NOT NULL,

    CONSTRAINT pk_users
        PRIMARY KEY (id),

    CONSTRAINT uk_users_telegram_user_id
        UNIQUE (telegram_user_id),

    CONSTRAINT uk_users_telegram_group_id
        UNIQUE (telegram_group_id),

    CONSTRAINT uk_users_username
        UNIQUE (username)
);