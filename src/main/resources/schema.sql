create table products
(
    id       bigint auto_increment,
    name     varchar(100),
    price    int,
    imageUrl varchar(255),
    acceptedByMD BOOLEAN,
    primary key (id)
);