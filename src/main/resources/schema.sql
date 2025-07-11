CREATE TABLE product
(
    id       BIGINT AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    price    INT NOT NULL,
    imageUrl VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE member
(
    id       BIGINT AUTO_INCREMENT,
    email    VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role     VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE wishlist
(
    id        BIGINT AUTO_INCREMENT,
    memberId  BIGINT NOT NULL,
    productId BIGINT,
    PRIMARY KEY (id)
);

-- PRIMARY KEY == UNIQUE + NOT NULL
-- UNIQUE는 NULL을 허용한다! NOT NULL을 함께 써야함!!
