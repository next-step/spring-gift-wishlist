# spring-gift-wishlist

## [STEP 0] 기본 코드 준비
- 이전 미션(상품 관리-스프링 입문)의 스켈레톤 코드를 기반으로 본격적인 개발 시작 전 환경 세팅

## [STEP 1] 유효성 검사 및 예외 처리
상품을 추가하거나 수정하는 경우, 클라이언트로부터 잘못된 값이 전달될 수 있다. 잘못된 값이 전달되면 클라이언트가 어떤 부분이 왜 잘못되었는지 인지할 수 있도록 응답을 제공한다.

### 상품 검증 규칙
- **길이**: 공백 포함 최대 15자까지 허용
- **허용 특수문자**: `(`, `)`, `[`, `]`, `+`, `-`, `&`, `/`, `_`
- **금지 특수문자**: 상기 이외의 모든 특수문자
- **‘카카오’ 키워드**: "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능

## [STEP 2] 회원 로그인
사용자가 회원 가입, 로그인, 추후 회원별 기능을 이용할 수 있도록 구현한다.

- 회원은 이메일과 비밀번호를 입력하여 가입할 수 있다.
- 로그인 요청 시, 등록된 이메일/비밀번호가 일치하면 **JWT 토큰**이 응답으로 반환된다.
- JWT는 `Authorization` 헤더를 통해 이후 요청에서 사용된다.
- 토큰 생성은 [`io.jsonwebtoken`](https://github.com/jwtk/jjwt) 라이브러리를 이용해 직접 구현한다.
- 회원을 조회, 추가, 수정, 삭제할 수 있는 관리자 화면을 구현한다.

- 회원가입
  - Request
  ```
    POST /api/members/register HTTP/1.1
    Content-Type: application/json
    host: localhost:8080
    
    {
         "email": "admin@email.com",
         "password": "password"
    }
    ```
  - Response
    ```
    HTTP/1.1 201
    Content-Type: application/json

    {
        "token": ""
    }
    ```

- 로그인
    - Request
  ```
    POST /api/members/login HTTP/1.1
    Content-Type: application/json
    host: localhost:8080
    
    {
         "email": "admin@email.com",
         "password": "password"
    }

    ```
    - Response
      ```
      HTTP/1.1 200
      Content-Type: application/json
    
      {
           "token": ""
      }
      ```