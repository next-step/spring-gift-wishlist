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
    memberId bigint not null,
    productId bigint not null,
    productCnt bigint not null check ( productCnt > 0 ),

    foreign key (memberId) references members(id) on delete cascade,
    foreign key (productId) references products(id) on delete cascade ,

    unique(memberId, productId)
);