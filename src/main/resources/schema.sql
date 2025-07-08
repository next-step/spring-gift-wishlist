DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS "user";

CREATE TABLE product
(
    productId BIGINT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     INTEGER      NOT NULL,
    imageURL  VARCHAR(500)

);

CREATE TABLE users
(
    user_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_role VARCHAR(100) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL
);


