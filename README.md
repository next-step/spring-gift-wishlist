# spring-gift-wishlist

## 0단계 - 기본 코드 준비
- 상품 관리 코드를 spring-gift-wishlist 프로젝트로 옮겨 온다.

## 1단계 - 유효성 검사 및 예외 처리
상품을 추가, 수정 시의 예외 처리
- 상품 이름은 공백 포함 최대 15자까지 입력 가능
- 특수 문자
  - (), [], +, -, &, /, _ 만 가능, 그 외 사용 불가
- "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능

## 2단계 - 회원 로그인
사용자가 회원 가입, 로그인, 추후 회원별 기능을 이용할 수 있도록 구현한다.
- 회원은 이메일과 비밀번호를 입력하여 가입한다.

  - 회원 가입 Request
      ```
      POST /api/members/register HTTP/1.1
      Content-Type: application/json
      host: localhost:8080

      {
        "email": "admin@email.com",
        "password": "password"
      }
      ```
      - 회원 가입 Response
      ```
      HTTP/1.1 201
      Content-Type: application/json
  
      {
        "token": ""
      }
      ```
    
- 토큰을 받으려면 이메일과 비밀번호를 보내야 하며, 가입한 이메일과 비밀번호가 일치하면 토큰이 발급된다.
  - 로그인 Request
  ```
  POST /api/members/login HTTP/1.1
  Content-Type: application/json
  host: localhost:8080

  {
    "email": "admin@email.com",
    "password": "password"
  }
  ```
  - 로그인 Response
  ```
  HTTP/1.1 200
  Content-Type: application/json

  {
      "token": ""
  }
  ```
