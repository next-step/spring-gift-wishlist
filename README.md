# spring-gift-wishlist

<br>

## 목차

0. [0단계 - 기본 코드 준비](#step-0---기본-코드-준비)
1. [1단계 - 유효성 검사 및 예외 처리](#step-1---유효성-검사-및-예외-처리)
2. [2단계 - 회원 로그인](#step-2---회원-로그인)

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

---

<br>

## Step 2 - 회원 로그인

---

#### 기능 요구 사항

- 사용자가 회원 가입, 로그인, 추후 회원별 기능을 이용할 수 있도록 구현
- 이메일, 비밀번호를 입력하여 회원 가입을 진행
- 이메일 중복 및 로그인 실패 시 적절한 예외 처리


#### 구현 상세 설명

- `POST /api/members/register` → 회원 가입
- `POST /api/members/login` → 로그인 및 토큰 발급

<br>

- 토큰을 생성하는 방법으로 `JJWT` 라이브러리를 사용
    + `JwtUtil` 클래스를 만들어 토큰 생성 코드를 재사용할 수 있도록 함
    + 추후 다른 메서드를 추가하여 `Filter`를 적용하는데도 도움을 줄 수 있음
- 커스텀 예외 클래스 추가 (`LoginFailedException`, `DuplicateEmailException`)
- `schema.sql`에 member 테이블 추가 코드 작성, 테스트를 위해 `data.sql`에 멤버 데이터 삽입