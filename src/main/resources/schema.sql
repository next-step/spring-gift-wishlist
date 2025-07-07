CREATE TABLE product
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(15)   NOT NULL,
    price     INT           NOT NULL CHECK (price >= 0),
    image_url VARCHAR(1024) NOT NULL
);