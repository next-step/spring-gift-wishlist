DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS members;

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    price NUMERIC(20),
    image_url VARCHAR(1000)
);

CREATE TABLE members (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    role VARCHAR(30) DEFAULT 'USER' NOT NULL
);