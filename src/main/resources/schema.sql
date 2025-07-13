CREATE TABLE product
(
    product_id BIGINT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
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

CREATE TABLE wishes
(
    wish_id      BIGINT                                 NOT NULL,
    user_id      BIGINT REFERENCES users (user_id)      NOT NULL,
    product_name VARCHAR(100) REFERENCES product (name) NOT NULL,
    quantity     INTEGER                                NOT NULL,
    UNIQUE (user_id, product_name)
);
