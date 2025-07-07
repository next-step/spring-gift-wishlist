CREATE TABLE products (
                          id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
                          name        VARCHAR(255)    NOT NULL UNIQUE,
                          price       INT             NOT NULL,
                          image_url   VARCHAR(4000)    NOT NULL
);

CREATE TABLE members (
                         id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
                         email       VARCHAR(255)    NOT NULL UNIQUE,
                         password    VARCHAR(255)    NOT NULL,
                         role        VARCHAR(50)     NOT NULL
);