#  spring-product-api

스프링 부트를 활용한 **상품(Product)** 관리 REST API 프로젝트입니다.  
상품 등록, 조회, 수정, 삭제 기능을 제공합니다.
<br><br>
---
## 추가 구현 기능(07.09)
- **커스템 예외처리 기능**: InvaildLoginException, UserNotFoundException 추가
- **비밀번호 암호화**: Spring Security를 사용못하기 때문에 자체 암호화 클래스 PasswordHasher 추가(SHA-256)
- **User Login 테스트 코드(E2E 테스트)**: JUnit5/RestClient를 통한 테스트 코드 작성.
- **Jwt 구현**
- **Login page 구현**: Spring Security를 사용할 수 없기 때문에(제이슨 강사님 강조) 시큐리티에서 제공하는 로그인 페이지를 사용할 수 없기 때문에 직접 구현

## 오류 해결(07.09)
- 

## 추가 예정 기능 
- Role을 현재 추가만 해놓은 상태인데, 후에 역할별로 들어가는 페이지를 다르게 설정하게끔 유도(인터셉터 이용).
- 회원가입 기능 구현(현재는 H2 DB에 default로 넣어놓은 사람만 로그인 가능)

---

## 구현 기능

###  상품 목록 조회
- **URL**: `GET /products`
- **설명**: 등록된 모든 상품 목록을 조회합니다.

---

###  상품 단건 조회
- **URL**: `GET /products/{id}`
- **설명**: ID에 해당하는 상품 정보를 조회합니다.

---

###  상품 추가
- **URL**: `POST /products`
- **설명**: 새로운 상품을 등록합니다.
- **요청 바디 예시**:
```json
{
  "name": "초코 케이크",
  "price": 5000,
  "imageUrl": "https://example.com/choco.jpg"
}
```
###  상품 삭제
- **URL**: `DELETE /products/{id}`
- **설명**: 지정한 ID의 상품을 삭제합니다.

---

## 관리자 페이지(Thymeleaf 기반)

### 상품 목록 (홈 화면)

- **URL**: GET /product-page
- **설명**: 관리자용 상품 리스트 페이지(HTML 기반)

---

### 상품 등록 폼

- **URL**: GET /product-page/new   
- **설명**: 새로운 상품을 등록하는 폼 페이지  

---

### 상품 수정 폼

- **URL**: GET /product-page/{id}  
- **설명**: 기존 상품 정보를 수정하는 폼 페이지

---

### 상품 삭제 요청


- **URL**: POST /product-page/{id}/delete   
- **설명**: HTML 페이지에서 상품 삭제 요청을 전송합니다


### 기술 스택
Java 21

Spring Boot 3.5.3

Spring Web (REST API)

Spring JDBC (JdbcClient)

Thymeleaf (관리자 페이지용)

H2 Database (in-memory)

JUnit5 (E2E 테스트 코드 작성)

Jwt(Spring Security 사용 X): refreshToken accessToken을 이용한 회원 로그인

