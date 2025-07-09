CREATE TABLE productDto (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         price INT,
                         image_url VARCHAR(500)
);
CREATE TABLE users (
                        id BIGINT AUTO_INCREMENT,
                        email VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
                        password VARCHAR(255) NOT NULL,
                        name VARCHAR(50),
                        created_at DATETIME(6),
                        updated_at DATETIME(6)
);
