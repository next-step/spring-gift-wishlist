# wishlist

## 🛠 0. 기본 코드 준비

- 상품 관리 코드 이동

## 🛠 1. 유효성 검사 및 예외 처리

- 상품 등록/수정 요청 시 유효성 검사 기능 구현
- 유효성 검사 실패 시 클라이언트에 에러 메시지 반환

## 🛠 2. 회원 로그인

- 회원 도메인 구현
    - Member entity: id, email, password
    - MemeberRequestDto, MemberResponseDto
- 회원가입 API 구현(POST /api/members/register)
- 로그인 API 구현(POST /api/members/login)
- 토큰 생성, 관리 구현
- 관리자 회원 관리 화면 구현

    - 조회, 추가, 수정, 삭제
