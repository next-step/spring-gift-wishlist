CREATE TABLE product
(
    product_id BIGINT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    price      INTEGER      NOT NULL,
    image_url  VARCHAR(500)

);

CREATE TABLE users
(
    user_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_role VARCHAR(100) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL
);

CREATE TABLE wish_list
(
    wish_id    BIGINT,
    user_id    BIGINT REFERENCES users (user_id),
    product_id BIGINT REFERENCES product (product_id),
    quantity   INTEGER default (0)
);
