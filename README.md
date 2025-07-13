# Spring-Gift-Wishlist

# step1 유효성 검사 및 예외 처리

## 요구 사항

상품을 추가하거나 수정하는 경우, 클라이언트로부터 잘못된 값이 전달될 수 있다. 잘못된 값이 전달되면 클라이언트가 어떤 부분이 왜 잘못되었는지 인지할 수 있도록 응답을 제공한다.

- 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
- 특수 문자
- 가능: ( ), [ ], +, -, &, /, \_
- 그 외 특수 문자 사용 불가
- "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.

## 구현 기능

- [x] Validation(@vaild)을 적용하여 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있게한다.
- [x] Validation(@vaild)을 적용하여특수 문자 ( ), [ ], +, -, &, /, \_ 만 사용할 수 있고 그 외 특수 문자 사용 불가하게 한다
- [x] "카카오"가 포함된 문구가 입력으로 들어왔을때 담당 MD와 협의한 경우에만 사용할 수 있다고 알린다.
- [x] 관리자 페이지에서 잘못된 값이 들어왔을때 사용자가 알 수 있게 빨간색으로 주의 문구를 보여준다.
- [x] GlobalExceptionHandler만들어 에러를 관리한다.
- [x] 테스트 코드를 작성하여 테스트 한다.

# Step2 회원 로그인

## 요구 사항

사용자가 회원 가입, 로그인, 추후 회원별 기능을 이용할 수 있도록 구현한다.

- 사용자는 이메일과 비밀번호로 회원 가입할 수 있습니다.
- 토큰을 받으려면 이메일과 비밀번호를 보내야 하며, 가입한 이메일과 비밀번호가 일치하면 토큰이 발급된다.
- 토큰을 생성하는 방법에는 여러 가지가 있다. 방법 중 하나를 선택한다.
- (선택) 회원을 조회, 추가, 수정, 삭제할 수 있는 관리자 화면을 구현한다.

## 구현 기능

- [x] 사용자 정보를 저장할 Member table을 생성한다.
- [x] 이메일과 비밀번호 정보를 받아 저장한다.(회원가입)
- [x] 이메일과 비밀번호 정보를 받아 로그인 한다.
- [x] 토큰을 전달한다.
- [x] 회원가입과 로그인을 화면을 구현
- [x] Bearer 인증 방식을 사용하여 JWT 를 전달해 관리자와 일반 유저를 구분한다.

## Member Table

```sql
CREATE TABLE Member (
    id BIGINT AUTO_INCREMENT,
    email VARCHAR(50),
    password VARCHAR(255),
    role VARCHAR(10),
    PRIMARY KEY (id)
);
```

### 📑 컬럼 설명

| 컬럼명     | 타입           | 설명                                |
| ---------- | -------------- | ----------------------------------- |
| `id`       | `BIGINT`       | 회원의 고유 ID (자동 증가, 기본 키) |
| `email`    | `VARCHAR(50)`  | 회원 이메일 (로그인 ID로 사용)      |
| `password` | `VARCHAR(255)` | 비밀번호 저장용                     |
| `role`     | `VARCHAR(10)`  | 사용자 역할 (`USER`, `ADMIN` 등)    |

## API 명세

### 회원 가입

**Request**<br>
`POST /api/members/register HTTP/1.1`<br>
`Content-Type: application/json`<br>
`Host: localhost:8080`

```json
{
  "email": "admin@email.com",
  "password": "password"
}
```

**Response**<br>
`HTTP/1.1 201 Created`<br>
`Content-Type: application/json`

```json
{
  "token": "..."
}
```

### 로그인

**Request**<br>
`POST /api/members/login HTTP/1.1`<br>
`Content-Type: application/json`<br>
`Host: localhost:8080`

```json
{
  "email": "admin@email.com",
  "password": "password"
}
```

**Response**<br>
`HTTP/1.1 200 OK`<br>
`Content-Type: application/json`<br>

```json
{
  "token": "..."
}
```

# Step3 위시 리스트

## 요구사항

이전 단계에서 로그인 후 받은 토큰을 사용하여 사용자별 위시 리스트 기능을 구현한다.

- 위시 리스트에 등록된 상품 목록을 조회할 수 있다.
- 위시 리스트에 상품을 추가할 수 있다.
- 위시 리스트에 담긴 상품을 삭제할 수 있다.

## 구현 기능

- [x] 위시리스트 정보를 저장할 Wish table을 생성한다.
- [x] 로그인 이후 토큰을 받고 추가할 상품과 갯수를 받아 저장한다
- [x] 토큰을 받아 유저를 확인후 유저에 맞는 위시리스트 목록을 보여준다.
- [x] 위시리스트 삭제 한다.

## 구현 방법

LoginMemberArgumentResolver를 작성하여 컨트롤러에서 @LoginMember Member member 를 사용하여 토큰에서 id를 찾아 member를 가져오게 하였다.
Wish table은 외래키를 설정하여 user마다 위시리스트를 가지게 저장한다.

## Wish Table

사용자의 위시리스트 정보를 저장하는 테이블입니다.  
각 항목은 어떤 사용자가 어떤 상품을 몇 개 찜했는지를 나타냅니다.

```sql
CREATE TABLE Wish (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    product_id BIGINT,
    quantity INT,

    FOREIGN KEY (member_id) REFERENCES Member(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);

```

| 컬럼명       | 타입   | 설명                                        |
| ------------ | ------ | ------------------------------------------- |
| `id`         | BIGINT | 위시리스트 항목 고유 ID (Primary Key)       |
| `member_id`  | BIGINT | 위시리스트를 등록한 사용자 ID (FK → Member) |
| `product_id` | BIGINT | 찜한 상품의 ID (FK → Product)               |
| `quantity`   | INT    | 사용자가 원하는 상품 수량                   |

## 🎯 WishList API 명세

사용자가 로그인한 상태에서 본인의 위시리스트를 관리할 수 있는 API입니다.
모든 요청은 `Authorization: Bearer {JWT}` 헤더를 필요로 합니다.

---

### 1. 위시리스트에 상품 추가

- **URL**: `POST /wishes`
- **설명**: 로그인한 사용자의 위시리스트에 상품을 추가합니다.
- **요청 헤더**:
  - `Authorization: Bearer {token}`
- **요청 바디** (JSON):

```json
{
  "productId": 1,
  "quantity": 3
}
```

응답:
201 Created

### 2. 위시리스트 목록 조회

- **URL**: `GET /wishes`
- **설명**: 로그인한 사용자의 위시리스트 상품 목록을 조회합니다.
- **요청 헤더**:

  - `Authorization: Bearer {token}`

- **응답**:

  - `200 OK`

- **응답 바디 예시**:

```json
[
  {
    "wishId": 1,
    "productId": 1,
    "productName": "상품 A",
    "price": 10000,
    "imageUrl": "http://example.com/image.png",
    "quantity": 2
  },
  {
    "wishId": 2,
    "productId": 2,
    "productName": "상품 B",
    "price": 5000,
    "imageUrl": "http://example.com/image2.png",
    "quantity": 1
  }
]
```

### 3. 위시리스트 상품 삭제

- **URL**: `DELETE /wishes/{id}`
- **설명**: 위시리스트에서 특정 항목을 삭제합니다.

- **요청 헤더**:

  - `Authorization: Bearer {token}`

- **경로 변수**:

  - `id` : 삭제할 위시 항목의 ID

- **응답**:

  - `204 No Content`

  ### 4. 위시리스트 수량 수정

- **URL**: `PATCH /wishes/{id}`
- **설명**: 위시리스트에서 특정 항목의 수량을 수정한다.

- **요청 본문**:

```json
{
  "quantity": 2
}
```

- **응답**:
  - `200 OK`
