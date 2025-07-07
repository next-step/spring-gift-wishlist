create table product
(
    id bigint AUTO_INCREMENT,
    name varchar(100),
    imageUrl varchar(255),
    primary key (id)
);

create table user
(
    id bigint AUTO_INCREMENT,
    email varchar(255),
    password varchar(255),
    primary key (id)
)