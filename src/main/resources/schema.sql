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
    email    varchar(255) unique,
    password varchar(255),
    role     varchar(100),
    primary key (id)
);

create table wishes
(
    id        bigint auto_increment,
    productId bigint REFERENCES products (id) on delete cascade,
    memberId  bigint REFERENCES members (id) on delete cascade,
    quantity  int,
    primary key (id)
);