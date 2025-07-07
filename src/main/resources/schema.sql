DROP TABLE IF EXISTS item;

CREATE TABLE item (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL,
                      price INT NOT NULL,
                      image_url VARCHAR(1000) NOT NULL
);

DROP TABLE IF EXISTS users;

CREATE TABLE users (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      nickname VARCHAR(100) NOT NULL unique
);
