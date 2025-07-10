CREATE TABLE product
(
    id    bigint auto_increment primary key,
    name  varchar(255),
    price bigint,
    url   varchar(255)
);

CREATE TABLE member
(
    id       bigint auto_increment primary key,
    email    varchar(255),
    password varchar(255),
    name     varchar(255),
    role     varchar(255)
);

CREATE TABLE wishlist
(
    id        bigint auto_increment primary key,
    member_id  bigint,
    product_id bigint,
    quantity  bigint
);