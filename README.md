# spring-gift-wishlist

## 위시 리스트

### 🚀 0단계 - 기본 코드 준비

- [x] 상품 관리 코드를 옮기기

### 🚀 1단계 - 유효성 검사 및 예외 처리

상품을 추가하거나 수정하는 경우, 클라이언트로부터 잘못된 값이 전달될 수 있다.
잘못된 값이 전달되면 클라이언트가 어떤 부분이 왜 잘못되었는지 인지할 수 있도록 응답을 제공한다.

#### 🛠 구현할 기능 목록

- 상품 추가/수정 API
- [x] 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
- [x] 특수 문자
    - 가능: ( ), [ ], +, -, &, /, _
    - 그 외 특수 문자 사용 불가
- [x] "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.
  -> 검사에 통과하지 못했을 시: 400 Bad Request

- [x] E2E 테스트 코드 작성하기(CRUD)

- [x] 상품 추가 API
    - [x] 단건 상품 추가
        - [x] **Request**: POST /api/products
          ```json
          {
              "name": "아이스 카페 아메리카노 T",
              "price": 4500,
              "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
          }
          ```
        - [x] **예외**:
            - name, price, imageUrl 중 하나라도 값이 존재하지 않을 때: 400 Bad Request

- [x] 상품 조회 API
    - [x] 전체 상품 조회
        - [x] **Request**: GET /api/products

    - [x] 단건 상품 조회
        - **Request**: GET /api/products/{productId}
        - [x] **예외**:
            - 데이터베이스에 productId가 존재하지 않을 때: 404 Not Found

- [x] 상품 수정 API
    - [x] 단건 상품 전체 수정
        - [x] **Request**: PUT /api/products/{productId}
          ```json
          {
              "name": "아이스 카페 아메리카노 T",
              "price": 9000,
              "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
          }
          ```
        - [x] **예외**:
            - 데이터베이스에 productId가 존재하지 않을 때: 404 Not Found
            - name, price, imageUrl 중 하나라도 존재하지 않을 때: 400 Bad Request

- [x] 상품 삭제 API
    - [x] 단건 상품 삭제
        - [x] **Request**: DELETE /api/products/{productId}
        - [x] **예외**:
            - 데이터베이스에 productId가 존재하지 않을 때: 404 Not Found

### 🚀 2단계 - 회원 로그인

사용자가 회원 가입, 로그인, 추후 회원별 기능을 이용할 수 있도록 구현한다.

- [x] 회원은 이메일과 비밀번호를 입력하여 가입한다.
- [x] 토큰을 받으려면 이메일과 비밀번호를 보내야 하며, 가입한 이메일과 비밀번호가 일치하면 토큰이 발급된다.
- [x] 토큰을 생성하는 방법에는 여러 가지가 있다. 방법 중 하나를 선택한다.
- [x] (**선택**) 회원을 조회, 추가, 수정, 삭제할 수 있는 관리자 화면을 구현한다.

#### 🛠 구현할 기능 목록

- 회원 API

| URL                   | 메서드  | 기능    | 설명                  |
|-----------------------|------|-------|---------------------|
| /api/members/register | POST | 회원 가입 | 새 회원을 등록하고 토큰을 받는다. |
| /api/members/login    | POST | 로그인   | 회원을 인증하고 토큰을 받는다.   |

- [x] 회원 가입
    - [x] **Request**
        ```http
        POST /api/members/register HTTP/1.1
        Content-Type: application/json
        host: localhost: 8080
        ```
        ```json
        {
            "email": "admin@email.com",
            "password": "password"
        }
        ```

    - [x] **Response**
        ```http
        HTTP/1.1 201
        Content-Type: application/json
        ```
        ```json
        {
            "token": ""
        }
        ```

    - [x] **예외**:
        - email, password, name 중 하나라도 존재하지 않을 때: `400 Bad Request`

- [x] 로그인
    - [x] **Request**
        ```http
        POST /api/members/login HTTP/1.1
        Content-Type: application/json
        host: localhost:8080
        ```
        ```json 
        {
            "email": "admin@email.com",
            "password": "password"
        }
        ```

    - [x] **Response**
        ```http
        HTTP/1.1 200
        Content-Type: application/json
        ```
        ```json
        {
            "token": ""
        }
        ```

    - [x] **예외**:
        - email, password 중 하나라도 값이 존재하지 않을 때: `400 Bad Request`
        - email, password 중 하나라도 틀릴 때: `403 Forbidden`

- [x] (선택) 회원을 조회, 추가, 수정, 삭제할 수 있는 관리자 화면을 구현

### 🚀 3단계 - 위시 리스트

이전 단계에서 로그인 후 받은 토큰을 사용하여 사용자별 위시 리스트 기능을 구현한다.

- [x] 위시 리스트에 등록된 상품 목록을 조회할 수 있다.
- [x] 위시 리스트에 상품을 추가할 수 있다.
- [x] 위시 리스트에 담긴 상품을 삭제할 수 있다.

#### 🛠 구현할 기능 목록

- 위시 리스트 API

| URL	                                              | 메서드	    | 기능	                      | 설명                                |
|---------------------------------------------------|---------|--------------------------|-----------------------------------|
| /api/wishes                                       | POST	   | 위시 리스트 상품 추가	            | 회원의 위시 리스트에 상품을 추가한다.             |
| /api/wishes/{wishId}	                             | DELETE	 | 위시 리스트 상품 삭제	            | 회원의 위시 리스트에서 상품을 삭제한다.            |
| /api/wishes?page=0&size=10&sort=createdDate,desc	 | GET	    | 위시 리스트 상품 조회 (페이지네이션 적용) | 	회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다. |

- [x] 위시 리스트 상품 추가
    - [x] **Request**
        ```http
        POST /api/wishes HTTP/1.1
        Authorization: Bearer {JWT_TOKEN}
        Content-Type: application/json
        host: localhost: 8080
        ```
        ```json
        {
            "productId": 101
        }
        ```

    - [x] **Response**: 예상
        ```http
        HTTP/1.1 201
        Content-Type: application/json
        ```
        ```json
        {
            "wishId": 1,
            "memberId": 5,
            "productId": 101,
            "createdDate": "2025-07-09T15:00:00"
        }
        ```

    - [x] **예외**:
        - token이 존재하지 않을 때: `401 Unauthorized`
        - productId가 존재하지 않을 때: `400 Bad Request`

- [x] 위시 리스트 상품 조회
    - [x] **Request**
        ```http
        GET /api/wishes?page=0&size=10&createdDate,desc HTTP/1.1
        Authorization: Bearer {JWT_TOKEN}
        Content-Type: application/json
        host: localhost:8080
        ```

    - [x] **Response**: 예상
        ```http
        HTTP/1.1 200
        Content-Type: application/json
        ```
        ```json
        {
            "content": [
                {
                    "wishId": 1,
                    "productId": 101,
                    "productName": "Example Product A",
                    "createdDate": "2025-07-09T15:00:00"
                },
                {
                    "wishId": 2,
                    "productId": 102,
                    "productName": "Example Product B",
                    "createdDate": "2025-07-08T14:30:00"
                }
            ],
            "pageable": {
                "pageNumber": 0,
                "pageSize": 10
            },
            "totalElements": 2,
            "totalPages": 1
        }
        ```

    - [x] **예외**:
        - token이 존재하지 않을 때: `401 Unauthorized`
        - URL이 유효하지 않을 때: `400 Bad Request`

- [x] 위시 리스트 상품 삭제
    - [x] **Request**
        ```http
        DELETE /api/wishes/{wishId} HTTP/1.1
        Authorization: Bearer {JWT_TOKEN}
        Content-Type: application/json
        host: localhost:8080
        ```

    - [x] **Response**
        ```http
        HTTP/1.1 204
        ```

    - [x] **예외**:
        - token이 존재하지 않을 때: `401 Unauthorized`
        - wishId가 존재하지 않을 때: `404 Not Found`