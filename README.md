# spring-gift-wishlist

## API 명세

### 상품관리 RESTful API

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 요청 (Request) | 응답 (Response) |
| :--- | :--- | :--- | :--- | :--- |
| 상품 등록 | `POST` | `/api/products` | Body: `ProductRequestDto` (상품 정보) | **201 Created** Body 없음 |
| 전체 상품 조회 | `GET` | `/api/products` | 없음 | **200 OK** Body: `List<ProductResponseDto>` (상품 목록) |
| 특정 상품 조회 | `GET` | `/api/products/{productId}` | Path: `productId` | **200 OK** Body: `ProductResponseDto` (상품 상세 정보) |
| 상품 정보 수정 | `PUT` | `/api/products/{productId}` | Path: `productId` Body: `ProductRequestDto` (수정할 상품 정보) | **204 No Content** Body 없음 |
| 상품 삭제 | `DELETE` | `/api/products/{productId}` | Path: `productId` (상품 ID) | **204 No Content** Body 없음 |

### 상품관리 관리자 View
| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 설명 |
| :--- | :--- | :--- | :--- |
| 상품 목록 조회 | `GET` | `/admin/product` | 전체 상품 목록 조회 |
| 상품 상세 조회 | `GET` | `/admin/product/{productId}` | 상품 상세 정보 조회 |
| 상품 추가 | `POST` | `/admin/product/add` | 새 상품 등록 |
| 상품 수정 | `POST` | `/admin/product/edit/{productId}` | 상품 정보 수정 |
| 상품 삭제 | `DELETE` | `/admin/product/{productId}` | 상품 삭제 |

## 위시 리스트 - 요청과 응답 심화

### 0단계 - 기본 코드 준비

### 1단계 - 유효성 검사 및 예외 처리

- [x] 유효성 검사 로직 구현하기(검증기).
- [x] API 에 유효성 검사 적용
- [x] 상품관리 관리자 페이지에 적용

### 1단계 - 코드 리뷰 반영

- [x] 유효성 검사 실패시 모든 실패 항목 메시지에 포함하기
- [x] README.md 에 체크박스 사용해보기
- [x] 400 에러 테스트 코드 추가하기

### 2단계 - 회원 로그인

- [x] Member 도메인 구현, 스키마 추가
- [ ] MemberRepository 구현
- [ ] JWT 토큰 생성, 토근 정보 추출 구현
- [ ] MemberService 구현
- [ ] MemberController, 회원 관련 페이지 구현
- [ ] Member 예외 처리 구현

### 1단계 - 코드 리뷰 반영

- [x] 유효성 검사 실패시 모든 실패 항목 메시지에 포함하기
- [x] README.md 에 체크박스 사용해보기

# spring-gift-product (이전 구현)

## 상품관리 - 스프링 입문

### 1단계 - 상품 API

- [x] Product 도메인 구현
- [x] Product DTO 구현
- [x] Product Service 구현
- [x] Product Controller 구현
- [x] 공통 예외 처리 구현 (NOT_FOUND)

### 1단계 - 코드 리뷰 반영

- [x] 불필요한 주식 제거
- [x] 테스트용 코드 제거
- [x] 규칙 9: getter/setter/프로퍼티를 쓰지 않는다 - Product setter 제거
- [x] service 계층의 id, Map 객체를 repository 계층으로 이동

### 2단계 - 관리자 화면

- [x] 상품 리스트 조회 페이지 구현
- [x] 상품 추가 페이지 구현
- [x] 상품 상세 페이지 구현
- [x] 상품 수정 & 삭제 페이지 구현

### 2단계 - 코드 리뷰 반영

- [x] 주석 제거
- [x] 불필요한 생성자 제거
- [x] Optional 예외 처리 refactoring
- [x] 매직 넘버 제거

### 3단계 - 데이터베이스 적용

- [x] schema.sql 작성
- [x] interface를 사용해서 기존 repo 추상화
- [x] JdbcProductRepository 구현
- [x] 테스트 코드 보완(ProductApiControllerTest)

### 3단계 - 코드 리뷰 반영

- [x] JdbcClient에서 사용할 RowMapper 직접 구현하고 setter와 기본 생성자 제거
- [x] 사용하지 않는 메소드 제거
- [x] validation 사용자 경험 개선(500 error -> forwarding)