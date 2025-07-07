# spring-gift-wishlist

## 2단계 - 회원 로그인

- [ ] **의존성 추가:** JWT 라이브러리(`jjwt`) 의존성을 추가한다.
- [ ] **데이터베이스 스키마 변경:** 회원 정보를 저장할 `member` 테이블을 `schema.sql`에 추가한다.
- [ ] **회원 도메인 구현:** 회원(Member) 엔티티, DTO, Repository, Service를 구현한다.
- [ ] **JWT 유틸리티 구현:** 토큰 생성 및 관리를 위한 `JwtUtil` 클래스를 구현한다.
- [ ] **회원가입/로그인 API 구현:** `MemberController`에 `POST /api/members/register`와 `POST /api/members/login` 엔드포인트를 구현한다.
- [ ] **예외 처리:** 로그인 실패(`403 Forbidden`), 중복 가입 시 예외를 처리한다.