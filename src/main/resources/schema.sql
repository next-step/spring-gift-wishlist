CREATE TABLE products
(
    productId BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255)  NOT NULL,
    price     INT           NOT NULL,
    imageUrl  VARCHAR(1000) NOT NULL,
    mdConfirmed BOOLEAN NOT NULL DEFAULT FALSE
);

-- CREATE TABLE approved_products
-- (
--     id   BIGINT PRIMARY KEY AUTO_INCREMENT,
--     name VARCHAR(255) NOT NULL
-- );
--
-- INSERT INTO approved_products(id, name) VALUES (1, "카카오");