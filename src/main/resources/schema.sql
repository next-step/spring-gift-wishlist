CREATE TABLE products (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         price INT,
                         image_url VARCHAR(500)
);
CREATE TABLE users (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(50) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        name VARCHAR(50),
                        created_at DATETIME(6),
                        updated_at DATETIME(6)
);

CREATE TABLE wishlist (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_email VARCHAR(50),
                        product_id BIGINT,
                        FOREIGN KEY (user_email) REFERENCES users(email),
                        FOREIGN KEY (product_id) REFERENCES products(id)

);

