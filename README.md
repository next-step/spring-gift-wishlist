# spring-gift-wishlist

## 구현할 기능 목록 (0단계 - 코드복사)

- [x] 이전 과정 코드 복사

## 구현할 기능 목록 (1단계 - 유효성 검사 및 예외 처리)

- [x] `build.gradle`에 `validation` 의존성 추가
- [x] `ProductRequest` DTO에 상품명 유효성 검사 추가
    - [x] 이름 길이 15자 이하 제한
    - [x] 허용된 특수문자 외 사용 금지
    - [x] "카카오" 포함 금지 (Custom Validator)
- [x] Controller에 `@Valid` 애노테이션 적용
- [x] REST API를 위한 글로벌 예외 처리기 (`@RestControllerAdvice`) 구현
- [x] 관리자 페이지 폼 유효성 검사 실패 처리

## 구현할 기능 목록 (2단계 - 회원 로그인)

- [x] `build.gradle`에 JWT 라이브러리 의존성 추가
- [x] Member(회원) 엔티티 및 리포지토리 구현
- [x] 회원 가입 기능 구현
    - [x] `/api/members/register` 엔드포인트 생성
    - [x] 이메일, 비밀번호를 받아 회원 정보 저장
- [x] 로그인 기능 및 토큰 발급 구현
    - [x] `/api/members/login` 엔드포인트 생성
    - [x] 이메일, 비밀번호 검증 후 JWT 토큰 발급
- [ ] 인증/인가를 위한 컴포넌트 구현
    - [x] 토큰 유효성 검사 로직 추가
    - [ ] `Authorization` 헤더를 처리하는 인터셉터 또는 필터 구현
- [ ] 인증/인가 관련 예외 처리
    - [ ] 유효하지 않은 토큰(401 Unauthorized), 잘못된 로그인 정보(403 Forbidden) 응답 처리
- [ ] (선택) 회원 관리자 화면 구현