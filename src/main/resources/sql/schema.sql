CREATE TABLE products (
                          id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
                          name        VARCHAR(255)    NOT NULL,
                          price       INT             NOT NULL,
                          image_url   VARCHAR(4000)    NOT NULL
);