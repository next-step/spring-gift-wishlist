DROP TABLE products IF EXISTS;
CREATE TABLE products(id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255),
                      price BIGINT,
                      imageUrl VARCHAR(255));

DROP TABLE users IF EXISTS;
CREATE TABLE users(id BIGINT AUTO_INCREMENT PRIMARY KEY,
                   email VARCHAR(255) UNIQUE,
                   password VARCHAR(255),
                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                   role VARCHAR(255));

DROP TABLE wishes IF EXISTS;
CREATE TABLE wishes(id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    user_id BIGINT FOREIGN KEY REFERENCES users(id) ON DELETE CASCADE,
                    product_id BIGINT FOREIGN KEY REFERENCES products(id) ON DELETE CASCADE,
                    quantity BIGINT);