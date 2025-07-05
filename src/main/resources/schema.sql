Create table Product(
    id bigint AUTO_INCREMENT,
    name varchar(50),
    price int,
    imageUrl varchar(512),

    primary key (id)
);


Create table Member(
    id bigint AUTO_INCREMENT,
    email varchar(50),
    password varchar(255),
    role varchar(10),
    primary key (id)
);