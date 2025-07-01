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
| 조건 | 설명 |
|------|------|
| 필수 | `@NotBlank` |
| 형식 | `@Pattern(regexp = "^(http|https)://.*$")` |

---

## ⚙️ 예외 처리

모든 유효성 실패는 `@Valid` 기반 예외를 전역 핸들러에서 처리합니다.

```json
{
  "name": "상품명은 필수입니다.",
  "price": "가격은 0 이상이어야 합니다."
}