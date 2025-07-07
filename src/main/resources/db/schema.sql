CREATE TABLE product (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(20) NOT NULL,
        price BIGINT NOT NULL,
        image_url VARCHAR(1000),
        md_approved BOOLEAN NOT NULL DEFAULT FALSE COMMENT '담당 MD 협의 여부'
);
