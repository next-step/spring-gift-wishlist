create table product (
    id bigint primary key auto_increment not null,
    name varchar(255) not null,
    price int not null,
    quantity int not null
);

create table users (
    id bigint primary key auto_increment not null,
    email varchar(50) not null,
    password varchar(255) not null,
    role varchar(10) not null
);

create table wishlist (
    id bigint primary key auto_increment not null,
    user_id bigint not null,
    product_id bigint not null,
    foreign key (user_id) references users(id),
    foreign key (product_id) references product(id)
)