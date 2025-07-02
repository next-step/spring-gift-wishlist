DROP TABLE Gift;

CREATE TABLE Gift(
  id BIGINT NOT NULL,
  giftId BIGINT,
  giftName VARCHAR(255),
  giftPrice INT,
  giftPhotoUrl VARCHAR(255),
  isKakaoMDAccepted BOOLEAN,
  primary key(id)
);

commit;