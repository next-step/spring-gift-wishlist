CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    description TEXT NOT NULL,
    image_url VARCHAR(512) NOT NULL
);