CREATE TABLE product
(
    productId BIGINT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     INTEGER      NOT NULL,
    imageURL  VARCHAR(500)

);

CREATE TABLE user
(
    user_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);


