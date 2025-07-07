CREATE TABLE product (
                         id          BIGINT          NOT NULL AUTO_INCREMENT,
                         name        VARCHAR(255)    NOT NULL,
                         price       INT             NOT NULL,
                         image_url   VARCHAR(255),
                         PRIMARY KEY (id)
);

CREATE TABLE member (
                        id          BIGINT          NOT NULL AUTO_INCREMENT,
                        email       VARCHAR(255)    NOT NULL UNIQUE,
                        password    VARCHAR(255)    NOT NULL,
                        PRIMARY KEY (id)
);
