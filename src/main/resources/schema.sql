create table products
(
    id        bigint auto_increment primary key,
    name      varchar(255),
    price     decimal(10, 2),
    image_url varchar(512)
);

create table members
(
    id       bigint auto_increment primary key,
    email    varchar(255),
    password varchar(255)
);