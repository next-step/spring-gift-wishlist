#  spring-product-api

스프링 부트를 활용한 **상품(Product)** 관리 REST API 프로젝트입니다.  
상품 등록, 조회, 수정, 삭제 기능을 제공합니다.

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
### 기술 스택
Java 21

Spring Boot 3.5.3

Spring Web (REST API)

Spring JDBC (JdbcClient)

Thymeleaf (관리자 페이지용)

H2 Database (in-memory)