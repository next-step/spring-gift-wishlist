CREATE TABLE PRODUCTS (
    id CHAR(36) PRIMARY KEY,
    name varchar(255),
    price INT,
    imageUrl varchar(255)
);

CREATE TABLE USERS (
    id CHAR(36) PRIMARY KEY,
    email varchar(255),
    password varchar(255),
    salt varchar(64)
);

CREATE TABLE WISHLISTS (
    id BIGINT PRIMARY KEY,
    user_id CHAR(36),
    product_id CHAR(36),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);