create table product
(
    id bigint AUTO_INCREMENT,
    name varchar(100),
    imageUrl varchar(255),
    primary key (id)
);

create table member
(
    id bigint AUTO_INCREMENT,
    email varchar(255) unique,
    password varchar(100),
    primary key (id)
);

create table wish
(
    id bigint AUTO_INCREMENT,
    productId bigint,
    memberId bigint,
    quantity int
);