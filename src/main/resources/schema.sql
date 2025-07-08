create table product (
    id bigint auto_increment primary key,
    name varchar(255) not null,
    price bigint not null,
    image_url varchar(1000)
);

create table member (
    id bigint auto_increment primary key,
    email varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(50) not null
);