create table products
(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) not null ,
    price bigint not null ,
    imageUrl varchar(255) not null
);

create table members
(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(10) not null
)