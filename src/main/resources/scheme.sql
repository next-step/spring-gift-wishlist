CREATE TABLE product
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255),
    price INT,
    image VARCHAR(255),
    mdApproved BOOLEAN DEFAULT FALSE
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE wish (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid BIGINT NOT NULL,
    productid BIGINT NOT NULL,
    count BIGINT NOT NULL,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_product FOREIGN KEY (productid) REFERENCES product(id) ON DELETE CASCADE,
    CONSTRAINT uc_user_product UNIQUE (userid, productid)
);