create table product (
  id bigint auto_increment primary key,
  name varchar(255),
  price int,
  imageUrl varchar(255)
);

create table member (
  id bigint auto_increment primary key,
  email varchar(255),
  password varchar(255)
)