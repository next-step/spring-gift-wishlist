CREATE TABLE products
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

-- "카카오" 문구가 포함된 상품명을 등록할 수 있는 승인 리스트 테이블
CREATE TABLE approved_products
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);