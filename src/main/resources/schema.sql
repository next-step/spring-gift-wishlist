CREATE TABLE ITEM
(
    id          BIGINT          NOT NULL    AUTO_INCREMENT,
    name        VARCHAR(100)    NOT NULL,
    price       INT             NOT NULL,
    image_url   VARCHAR(255)    NOT NULL,
    Primary Key (id)
);

CREATE TABLE MEMBER
(
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(20)     NOT NULL,
    email       VARCHAR(50)     NOT NULL,
    password    VARCHAR(255)    NOT NULL,
    PRIMARY KEY (id)
)