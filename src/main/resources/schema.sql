create table product
(
    id bigint auto_increment primary key,
    name varchar(255) not null,
    price int not null,
    image_url varchar(2083) not null,
    validated bit not null
);

create table member
(
    identify_number bigint auto_increment primary key,
    email varchar(255) not null unique,
    password varchar(36) not null
);