insert into members (id, email, password, role) values (default, 'test@test.com', 'cGFzc3dvcmQxMjM=', 'USER');
insert into members (id, email, password, role) values (123, 'remove@test.com', 'cGFzc3dvcmQxMjM=', 'USER');
insert into products (id, name, price, image_url) values (default, 'Test Product', 1000, 'http://test.com');
insert into products (id, name, price, image_url) values (123, 'Test Product', 1000, 'http://test.com');
insert into wishItems (id, productId, quantity, memberId) values (default, 1, 2, 1);