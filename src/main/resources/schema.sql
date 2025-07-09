create table product (
    id bigint primary key auto_increment not null,
    name varchar(255) not null,
    price int not null,
    quantity int not null
);

create table users (
    id bigint primary key auto_increment not null,
    email varchar(50) not null,
    password varchar(30) not null,
    role varchar(10) not null
)