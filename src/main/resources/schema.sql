CREATE TABLE product
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255)
);

CREATE TABLE member
(
    uuid       CHAR(36) PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL
);

CREATE TABLE refresh_token
(
    token           CHAR(32) PRIMARY KEY,
    member_uuid     CHAR(36) NOT NULL,
    created_at      DATETIME   NOT NULL,
    expiration_date DATETIME   NOT NULL,
    FOREIGN KEY (member_uuid) REFERENCES member (uuid) ON DELETE CASCADE
);
