create table products(
    id bigint auto_increment,
    name varchar(255) not null,
    price int not null,
    image_url varchar(255) not null,
    primary key (id)
);

create table members(
    id bigint auto_increment,
    role varchar(10) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    primary key (id),
    unique (email)
);

create table wishList(
    id bigint auto_increment,
    memberid bigint not null,
    productid bigint not null,
    quantity int not null,
    primary key (id),
    foreign key (productid) references products(id),
    foreign key (memberid) references members(id)
);