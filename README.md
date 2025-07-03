# spring-gift-wishlist

<br>

## 목차

0. [0단계 - 기본 코드 준비](#step-0---기본-코드-준비)
1. [1단계 - 유효성 검사 및 예외 처리](#step-1---유효성-검사-및-예외-처리)

---

<br>

## Step 0 - 기본 코드 준비

---

#### 기능 요구 사항

- 상품 관리 코드 옮기기
- `미션 1 - step 3`의 코드 리뷰 피드백 반영 : Repository 레이어 생성

---

<br>

## Step 1 - 유효성 검사 및 예외 처리

---

#### 기능 요구 사항

- 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있음
- `( ), [ ], +, -, &, /, _` 이외의 특수문자는 사용이 불가함
- `카카오`가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능


#### 구현 상세 설명

- `ProductRequestDto`를 `record`에서 `class`로 변경
- `validation` 패키지의 어노테이션들을 통해 예외 처리 구현
- 유효성 검사 실패 시 발생하는 `MethodArgumentNotValidException`을 처리하기 위해 `GlobalExceptionHandler`에서 처리
- `"카카오"` 포함 문구에 대한 예외 처리 클래스인 `ForbiddenWordException` 추가 및 핸들러에서 처리하도록 구현