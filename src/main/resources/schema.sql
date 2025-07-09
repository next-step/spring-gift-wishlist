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
    -- password is not raw, so should be longer than actual input
    -- using BCrypt, the length is 60 characters. but we use 255 for future-proofing
    password varchar(255) not null,
    authority varchar(255) not null
);