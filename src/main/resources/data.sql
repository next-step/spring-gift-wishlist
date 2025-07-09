-- product
INSERT INTO product (id, name, price, imageUrl) VALUES (99997, 'pencil', 200, 'test.com/1.jpg');
INSERT INTO product (id, name, price, imageUrl) VALUES (99998, 'note', 1500, 'test.com/2.jpg');
INSERT INTO product (id, name, price, imageUrl) VALUES (99999, 'phone', 800000, 'test.com/3.jpg');

-- member
INSERT INTO member (id, email, password, role) VALUES (99997, 'test1@mail.com', '$2a$10$lpXAZ4wJB1HgmjFjKid9BuK1XNrVFheWMBoMTltPLb6NxD/kTqWZa', 'ROLE_USER'); -- pw: 123412!3
INSERT INTO member (id, email, password, role) VALUES (99998, 'test2@mail.com', '$2a$10$lpXAZ4wJB1HgmjFjKid9BuK1XNrVFheWMBoMTltPLb6NxD/kTqWZa', 'ROLE_USER'); -- pw: 123412!3
INSERT INTO member (id, email, password, role) VALUES (0, 'rootroot', '$2a$10$HqO3FZQxO5F0N9qUJM4Z1eETG1cn8GrEAqqMlHxXHyujHsJuqeIKS', 'ROLE_ADMIN'); -- pw: rootr@@t
