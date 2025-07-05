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

# 구현 기능

- [x] 사용자 정보를 저장할 Member table을 생성한다.
- [x] 이메일과 비밀번호 정보를 받아 저장한다.(회원가입)
- [x] 이메일과 비밀번호 정보를 받아 로그인 한다.
- [x] 토큰을 전달한다.


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
