CREATE TABLE PRODUCTS (
    id CHAR(36) PRIMARY KEY,
    name varchar(255),
    price INT,
    imageUrl varchar(255)
);

CREATE TABLE USERS (
    id CHAR(36) PRIMARY KEY,
    email varchar(255),
    password varchar(255)
);