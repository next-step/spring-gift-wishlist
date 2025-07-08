create table products (
    id bigint auto_increment primary key,
    name varchar(100),
    price int,
    imageUrl varchar(255)
);

create table members (
    id bigint auto_increment primary key,
    name varchar(100),
    email varchar(100),
    password varchar(100),
    role varchar(20) not null default 'USER'
);