CREATE TABLE product
(
    id       BIGINT AUTO_INCREMENT,
    name     VARCHAR(100),
    price    INT,
    imageUrl VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE member
(
    id      BIGINT AUTO_INCREMENT,
    email    VARCHAR(100),
    password VARCHAR(100),
    role     VARCHAR(20),
    PRIMARY KEY (id)
);
