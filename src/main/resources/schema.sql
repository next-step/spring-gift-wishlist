create table products(
    id bigint auto_increment,
    name varchar(255) not null,
    price int not null,
    image_url varchar(255) not null
);

create table members(
    id bigint auto_increment,
    email varchar(255) not null,
    password varchar(255) not null,
    primary key (id),
    unique (email)
);
