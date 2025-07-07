create table products
(
    id       bigint auto_increment,
    name     varchar(100),
    price    int,
    imageUrl varchar(255),
    primary key (id)
);

create table members
(
    id       bigint auto_increment,
    email    varchar(255),
    password varchar(255),
    role     varchar(100),
    primary key (id)
);