create table products(
    id bigint auto_increment primary key,
    name varchar(255) not null,
    price decimal(10,2) not null,
    img_url varchar(512) not null
);

create table members(
    id bigint auto_increment primary key ,
    email varchar(255) not null unique ,
    password varchar(255) not null,
    role varchar(20) not null
);

create table wish (
    id bigint auto_increment primary key,
    member_id bigint not null,
    product_id bigint not null,
    unique (member_id, product_id),
    foreign key (member_id) references members(id),
    foreign key (product_id) references products(id)
);
