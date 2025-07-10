TRUNCATE TABLE products;

ALTER TABLE products ALTER COLUMN id RESTART WITH 1;

TRUNCATE TABLE members;

ALTER TABLE members ALTER COLUMN id RESTART WITH 1;

INSERT INTO products (name, price, image_url) VALUES ('샘플 상품1', 10000, 'sample1.jpg');

INSERT INTO products (name, price, image_url) VALUES ('샘플 상품2', 20000, 'sample2.jpg');

INSERT INTO members (name, email, password) VALUES ('이름A', 'aaa@email.com', 'aaaaaaaa');

INSERT INTO members (name, email, password) VALUES ('이름B', 'bbb@email.com', 'bbbbbbbb');

