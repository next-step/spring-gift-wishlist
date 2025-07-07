DROP TABLE IF EXISTS Gift;

CREATE TABLE Gift(
  id BIGINT NOT NULL,
  giftId BIGINT,
  giftName VARCHAR(255),
  giftPrice INT,
  giftPhotoUrl VARCHAR(255),
  isKakaoMDAccepted BOOLEAN,
  primary key(id)
);

DROP TABLE IF EXISTS Users;

CREATE TABLE Users(
    id BIGINT NOT NULL,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    primary key(id)
);

commit;