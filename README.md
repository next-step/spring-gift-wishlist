#  spring-product-api

스프링 부트를 활용한 **상품(Product)** 관리 REST API 프로젝트입니다.  
상품 등록, 조회, 수정, 삭제 기능을 제공합니다.
  
<br><br>

## 추가 구현 기능(step1)
- **예외처리**: GlobalExceptionHandler, ErrorResponseDto, validation(Package)추가
  

## 추가 구현 기능(07.04)
- **예외처리 기능**: ErrorResponseDto, GlobalExceptionHandler 추가(RFC 7807 기반)
- **커스텀 어노테이션**
  - 특정 금칙어가 들어오는 것을 방지.
  - 공통 Validator를 통해 추가할 때마다 어노테이션을 만들어야하는 불편함을 방지.
  - 예외처리 발생 시 해당 에러 내용 alert(js)로 출력하게끔 반영.
-  **테스트 코드(E2E 테스트)**: JUnit5/RestClient를 통한 테스트 코드 작성.(Given - When - Then 구조 적용)

## 오류 해결(07.04)
- **예외처리 반영 X 오류**: POSTMAN에서 확인 시 잘 걸러내지만 페이지에서는 잘 걸러내지 못하는 오류가 있었다. PageController의 createProduct에서도 Valid를 추가하여 해결. 

## 추가 예정 기능 (step 1 머지 이후 진행)
- **회원 로그인 기능** : 회원 로그인 기능(admin과 일반 회원과의 차이점 두기) - ex. 관리자 시 관리자화면으로 가지게끔 유도.
- **추후 업데이트**: 예를 들어 목록 수정은 추가한 사람만, 목록 전체는 관리자만 보기 등.


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

