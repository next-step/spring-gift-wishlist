DROP TABLE IF EXISTS wish_list;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS members;

CREATE TABLE product (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price INTEGER NOT NULL,
  image_url VARCHAR(255)
);

CREATE TABLE members (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50)
);

CREATE TABLE wish_list (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  member_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  CONSTRAINT fk_wishlist_member FOREIGN KEY (member_id) REFERENCES members(id),
  CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES product(id),
  CONSTRAINT uq_member_product UNIQUE (member_id, product_id)
);
