create table products
(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) not null ,
    price bigint not null ,
    imageUrl varchar(255) not null
);

create table members
(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(10) not null
);

create table wishlist
(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    member_id bigint not null,
    product_id bigint not null,

    foreign key (member_id) references members(id) on delete cascade,
    foreign key (product_id) references products(id) on delete cascade ,

    unique(member_id, product_id)
);