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
);

CREATE TABLE WISHLIST
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    member_id   BIGINT      NOT NULL,
    item_id     BIGINT      NOT NULL,
    created_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (member_id, item_id),
    FOREIGN KEY (member_id) REFERENCES MEMBER(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES ITEM(id) ON DELETE CASCADE
);