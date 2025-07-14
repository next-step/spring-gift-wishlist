INSERT INTO product (name, price, image_url)
VALUES ('딸기 케이크', 5500, 'https://example.com/strawberry.jpg');

INSERT INTO users (email, password, role)
VALUES ('example@example.com', 'f47c4b720cd560809b27592e4933f897170e7cfe999a3f80d04c7b2887aa8843', 'admin');

INSERT INTO refresh_token (user_id, refresh_token)
VALUES (1, 'some-refresh-token-value');