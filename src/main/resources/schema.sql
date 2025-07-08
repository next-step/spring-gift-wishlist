CREATE TABLE product
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(2048) NOT NULL,
    status    VARCHAR(30)   NOT NULL
);

CREATE TABLE member
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    email     VARCHAR(255)  NOT NULL UNIQUE,
    password  VARCHAR(255)  NOT NULL,
    role      VARCHAR(20)   NOT NULL
);