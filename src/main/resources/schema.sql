drop table products if exists;

create table products
(
    id          bigint auto_increment primary key,
    name        varchar(255) not null,
    price       bigint not null,
    imageUrl    varchar(1024) not null,
    approved    boolean default true not null,
    description varchar(1024)
);

drop table members if exists;

create table members
(
    id          bigint auto_increment primary key,
    email       varchar(255) not null unique,
    password    varchar(255) not null,
    role        varchar(50) not null default 'USER'
);