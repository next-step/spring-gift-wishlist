create table product(
    id bigint auto_increment primary key,
    name varchar(255) not null,
    price int not null,
    image_url varchar(255)
);

create table members(
    id bigint auto_increment primary key,
    email varchar(255) not null unique,
    pwd varchar(255) not null
);

create table wish(
    id bigint auto_increment primary key,
    member_id bigint not null,
    product_id bigint not null,
    unique(member_id, product_id)
);