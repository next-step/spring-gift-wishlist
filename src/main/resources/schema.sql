create table products (
    id bigint auto_increment primary key,
    name varchar(100),
    price int,
    imageUrl varchar(255)
);

CREATE TABLE members (
     id varchar(50) primary key,
     email varchar(100) not null unique ,
     password varchar(255) not null,
     name varchar(50) not null,
     address varchar(255) not null,
     role varchar(20) DEFAULT 'USER'
);
