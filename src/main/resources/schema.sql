create table product (
  id bigint auto_increment primary key,
  name varchar(255),
  price int,
  imageUrl varchar(255)
);

create table member (
  email varchar(255) primary key,
  password varchar(255)
)