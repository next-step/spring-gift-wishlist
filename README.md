# spring-gift-wishlist

## 0️⃣ 0단계 - 기본 코드 준비
- 상품 관리 코드를 옮겨 온다.

## 1️⃣ 1단계 - 유효성 검사 및 예외 처리
### 개발 예정 기능 목록
- [x] 상품 추가/수정 시 유효성 검사 기능 구현
  - [x] 상품 이름 길이 제한: 공백 포함 최대 15자까지 입력 가능
  - [x] 상품 이름에 사용할 수 있는 특수 문자 제한
    - 허용: ( ), [ ], +, -, &, /, _
    - 비허용: 그 외 모든 특수 문자
  - [x] 상품 이름에 "카카오"가 포함된 경우, MD 승인 여부를 확인하는 로직 추가

## 2️⃣ 2단계 - 회원 로그인
### 개발 예정 기능 목록
- [x] 회원 기능
  - [x] 회원 테이블 생성
  - [x] 이메일과 비밀번호를 입력받아 회원 가입 API 구현 (`POST /api/members/register`)
    - [x] 비밀번호는 BCrypt 알고르즘을 이용해 암호화하여 저장
    - [x] 회원가입 성공 (`201 Created`)
  - [x] 로그인 API 구현 (`POST /api/members/login`)
    - [x] 이메일과 비밀번호가 일치하면 토큰 발급 (`200 OK`)

- [x] 인증 및 예외 응답 처리
  - [x] 이메일 중복 시 예외 처리 (`409 Conflict`)
  - [x] 이메일 또는 비밀번호가 불일치 시 예외 처리 (`401 Unauthorized`)
  - [x] 인증은 되었지만 권한이 없는 경우 (`403 Forbidden`)
  - [x] Authorization 헤더 또는 토큰이 유효하지 않은 경우 (`401 Unauthorized`)

## 3️⃣ 3단계 - 위시 리스트
### 개발 예정 기능 목록
- [ ] 위시 리스트 API
  - [x] 위시 리스트에 등록된 상품 조회 API (`GET /api/wishes`)
  - [ ] 위시 리스트에 상품 등록 API (`POST /api/wishes`)
  - [ ] 위시 리시트 상품 삭제 API (`DELETE /api/wishes/{productId}`)
    - [ ] 존재하지 않는 상품일 경우 예외 처리 (`404 Not Found`)

## 4️⃣ 기술 스택
- Java 17
- Spring Boot 3.5.3
- JSON
- Java Collection
- Thymeleaf
- Jwt
