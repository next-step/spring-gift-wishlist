create table products
(
    id        bigint auto_increment primary key,
    name      varchar(255) not null,
    price     int          not null check (price >= 0),
    image_url varchar(1000)
);

CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

