create table product
(
   id bigint auto_increment,
   name varchar(100),
   price int,
   image_url varchar(255),
   primary key (id)
);

create table member
(
   email varchar(100),
   password varchar(255),
   primary key (email)
);

