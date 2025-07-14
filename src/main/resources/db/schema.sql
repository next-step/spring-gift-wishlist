CREATE TABLE products (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL,
    image_url VARCHAR(255),
    PRIMARY KEY(id)
);

CREATE TABLE members (
    id BIGINT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE wishItems (
    id BIGINT AUTO_INCREMENT,
    productId BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    memberId BIGINT NOT NULL,
    PRIMARY KEY(id)
);