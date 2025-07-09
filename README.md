# spring-gift-wishlist

### 미션 1 - 상품 관리 (spring-gift-product) README : https://github.com/5seonjae/spring-gift-product/blob/step3/README.md

## Step 1 - 유효성 검사 및 예외 처리

### 기존 기능

- [x] 상품 등록 요청 시 유효성 검사
    - [x] 상품명은 공백 불가 (`@NotBlank`)
    - [x] 가격은 0 이상 (`@Min(0)`, `@NotNull`)
    - [x] 이미지 URL은 http/https로 시작하는 유효한 URL만 허용 (`@Pattern`)
- [x] 상품 수정 요청 시 유효성 검사 동일하게 적용
- [x] 유효성 실패 시 상세 오류 메시지 반환 (`MethodArgumentNotValidException`)
- [x] `@ControllerAdvice`를 활용한 전역 예외 처리
    - [x] `IllegalArgumentException`
    - [x] `NoSuchElementException`
    - [x] `MethodArgumentNotValidException`
- [x] 도메인 객체(`Product`)에서도 생성자 수준 유효성 검증 추가
- [x] HTML 등록/수정 폼에서 `@Valid` 및 `BindingResult` 기반 검증 처리
- [x] 유효성 검증 실패 시 원래 폼으로 되돌아가며 사용자 입력 유지

### ✅ 추가 기능 요약

- [x] 상품명, 가격, 이미지 URL에 대한 유효성 검증
- [x] 유효성 검사 실패 시 명확한 에러 메시지 반환
- [x] 상품명에 "카카오"가 포함된 경우, 사전에 승인된 상품만 등록 가능
- [x] 테스트 코드 구현

### 📌 변경 사항 요약

### 1. View 레이어 테스트 추가 (`ProductViewControllerTest`)
- 상품 등록 폼 진입, 등록 처리(성공·유효성 오류) 테스트 구현
- 상품 목록 조회 테스트 구현
- 상품 상세 조회(성공·없는 ID 리다이렉트+플래시) 테스트 구현
- 상품 수정 폼 진입, 수정 처리(성공·유효성 오류·‘카카오’ 승인 로직) 테스트 구현
- 상품 삭제 처리(성공) 테스트 구현

### 2. REST API 테스트 추가 (`ProductControllerTest`)
- 상품 등록 API: 성공, 유효성 오류(빈값·길이·특수문자·URL), JSON 파싱 오류(타입 불일치·값 누락)
- 상품 조회 API: 단건 조회(200·404·잘못된 ID 형식) 및 전체 조회(200) 테스트 구현
- 상품 수정 API: 성공, 404·잘못된 ID 형식, 유효성 오류, JSON 파싱 오류 테스트 구현
- 상품 삭제 API: 성공(204), 404·잘못된 ID 형식 테스트 구현

### 3. 전역 예외 처리기 추가 (`GlobalExceptionHandler`)
- `HttpMessageNotReadableException` → JSON 파싱 오류 핸들러 (400 + 메시지)
- `MethodArgumentTypeMismatchException` → PathVariable/RequestParam 타입 불일치 핸들러 (400 + 메시지)

---

> 이 변경으로 E2E 수준의 API 검증과 View 렌더링 흐름 모두를 커버하며,  
> JSON 바디·경로 파라미터 타입 오류 및 유효성 검사 오류까지 일관된 방식으로 처리합니다.

## 🔍 유효성 검증 항목

### 🛍️ 상품명 (name)
| 조건 | 설명 |
|------|------|
| 필수 | `@NotBlank` |
| 최대 길이 | `@Size(max = 15)` |
| 허용 특수문자만 입력 가능 | `@Pattern` 정규식 사용 *(예: 한글, 영어, 숫자, 공백 일부 특수문자)* |
| **"카카오" 포함 시** | 승인된 상품인지 추가 검증 진행 |

### 💰 가격 (price)
| 조건 | 설명 |
|------|------|
| 필수 | `@NotNull` |
| 최소값 | `@Min(0)` |

### 🖼️ 이미지 URL (imageUrl)
| 조건 | 설명                                          |
|------|---------------------------------------------|
| 필수 | `@NotBlank`                                 |
| 형식 | `@Pattern(regexp = "^(http\|https)://.*$")` |

---

## ⚙️ 예외 처리

모든 유효성 실패는 `@Valid` 기반 예외를 전역 핸들러에서 처리합니다.

```json
{
  "name": "상품명은 필수입니다.",
  "price": "가격은 0 이상이어야 합니다."
}
```

### 수정 사항

- [x] JdbcTemplate 기반 Repository 코드를 JdbcClient로 수정



---

## Step 2 - 회원 로그인

### 기능 목록

- 회원가입 (이메일/비밀번호)
- 로그인 (Basic 인증 → JWT 발급)
- Spring Security + JWT 필터 적용  

### API 명세

- 회원가입
  - `POST /api/members/register`
    - Request
    ```json
    {
      "email": "admin@email.com",
      "password": "password"
    }
    ```
    - Response (201 Created)
    ```json
    {
      "token": "<발급된 JWT 토큰>"
    }
    ```
    - 에러
      - 400 Bad Request : 입력 검증 실패
      - 409 Conflict : 이메일 중복

- 로그인
  - `POST /api/members/login`
    - Headers
    ```text
    Authorization: Basic base64({email}:{password})
    ```
    - Response (200 OK)
    ```json
    {
      "token": "<발급된 JWT 토큰>"
    }
    ```
    - 에러
      - 401 Unauthorized : 토큰이 없거나 형식이 잘못된 경우
      - 403 Forbidden : 로그인 시 아이디·비밀번호 불일치 / 비밀번호 찾기·변경 요청

### 테스트

- MockMvc를 이용한 단위·통합 테스트
- 회원가입 / 로그인 성공·실패 시나리오 커버

### 완료 체크리스트

- [x] 회원가입 API 구현 (POST /api/members/register)
- [x] 로그인 API 구현 (POST /api/members/login)
- [x] 회원가입/로그인 시나리오 테스트 작성
- [x] README에 API 명세 추가

### 선택 구현

- [ ] Spring Security 및 JWT 필터 설정
- [ ] 보호된 API 구현 및 테스트



---

## Step 3 - 위시 리스트

### ✅ 진행 체크리스트

- [ ] 테이블(`wish_items`) 생성 & 마이그레이션
- [ ] `WishRepository` 구현 (JdbcClient)
- [ ] `WishService` 비즈니스 로직
  - 상품 추가(중복 시 수량 +1)
  - 목록 조회
  - 삭제 / 수량 0 처리
- [ ] `WishController` REST API
  - `GET /api/wishes`
  - `POST /api/wishes`
  - `DELETE /api/wishes/{productId}`
- [ ] `@LoginMember`ArgumentResolver로 인증 회원 주입
- [ ] MockMvc • 통합 테스트 작성
- [ ] 위시 리스트 UI 화면 구현 (Thymeleaf)
  - `/wishes` : 로그인 사용자의 장바구니 찜 목록
- [ ] README 갱신 & 예제 스크린샷 추가

### 🗄️ DB 스키마

```sql
CREATE TABLE wish_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT    NOT NULL DEFAULT 1,
    UNIQUE KEY uk_member_product (member_id, product_id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

### 🔗 API 명세

- 위시 리스트 조회
  - Method : `GET`
  - Path : `/api/wishes`
  - 요청 Body : X
  - 성공 응답 : `{ productId, name, price, imageUrl, quantity }`
  - 상태 코드 : `200 OK`
  - 예시
  - ```Plain Text
    [
      {
        "productId": 3,
        "name": "카카오 프렌즈 볼펜",
        "price": 15000,
        "imageUrl": "https://image.com/item.jpg",
        "quantity": 2
      }
    ]
    ```

- 위시 리스트 상품 추가
  - Method : `Post`
  - Path : `/api/wishes`
  - 요청 Body : `{ "productId": 3, "quantity": 1 }`
  - 성공 응답 : X
  - 상태 코드 : `201 Created`
  - 예시
    ```Plain Text
    POST /api/wishes
    Authorization: Bearer eyJhbGciOi...
    Content-Type: application/json
  
    {
      "productId": 3,
      "quantity": 2
    }
    ```

- 위시 리스트 상품 제거
  - Method : `DELETE`
  - Path : `/api/wishes/{productId}`
  - 요청 Body : X
  - 성공 응답 : X
  - 상태 코드 : `204 No Content`

- 공통 헤더 : `Authorization: Bearer <JWT Access Token>`