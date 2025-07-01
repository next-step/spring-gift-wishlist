# spring-gift-wishlist

### 미션 1 - 상품 관리 (spring-gift-product) README : https://github.com/5seonjae/spring-gift-product/blob/step3/README.md

## Step 1 - 유효성 검사 및 예외 처리

### 구현 기능

- [x] 상품 등록 요청 시 유효성 검사
    - [x] 상품명은 공백 불가 (`@NotBlank`)
    - [ ] 상품명에 허용된 특수문자만 허용 (`( ), [ ], +, -, &, /, _`)
    - [ ] 상품명은 최대 15자 제한 (`@Size`)
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
- [ ] "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.
  - [ ] 커스텀 어노테이션 (`@IsValidKeyword`) 을 만들어 구현

### 추가 구현 기능

- [ ] 상품 페이지 레이아웃 구현
- [ ] 테스트 코드 구현
  - [ ] `Product`
  - [ ] `ProductService`
  - [ ] `ProductRepository`
  - [ ] `ProductController`
  - [ ] `GlobalExceptionHandler`