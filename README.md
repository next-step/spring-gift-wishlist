# spring-gift-wishlist

## 상품 기능

- 일반 기능
    - 상품 등록
    - 상품 상세 조회
    - 상품 수정
    - 상품 삭제
    - 상품 목록 조회
- 관리자 기능
    - 상품 조회
    - 상품 추가
    - 상품 수정
    - 상품 삭제
- 유효성 검증
    - 상품 이름: 최대 15자(공백 포함)
    - 허용 특수 문자: ( ), [ ], +, -, &, /, _
    - 비허용 특수 문자: 그 외 모든 특수 문자
    - "카카오" 포함 시 협의 필요

## 회원 기능

- 일반 기능
    - 회원가입
    - 로그인

## API 명세

### 상품 API

| URL                       | 메서드    | 기능       | 설명           |
|---------------------------|--------|----------|--------------|
| /api/products             | POST   | 상품 생성    | 새 상품 등록      |
| /api/products/{productId} | GET    | 상품 조회    | 특정 상품 정보 조회  |
| /api/products/{productId} | PUT    | 상품 수정    | 기존 상품 정보 수정  |
| /api/products/{productId} | DELETE | 상품 삭제    | 특정 상품 삭제     |
| /api/products             | GET    | 상품 목록 조회 | 모든 상품의 목록 조회 |

### 관리자 API

| URL                         | 메서드  | 기능        | 설명                      |
|-----------------------------|------|-----------|-------------------------|
| /admin/products             | GET  | 상품 목록 페이지 | 상품 목록 HTML 반환           |
| /admin/products/new         | GET  | 상품 등록 페이지 | 빈 상품 등록 폼 HTML 반환       |
| /admin/products/new         | POST | 상품 등록 처리  | HTML 폼 데이터로 상품 등록       |
| /admin/products/{id}        | GET  | 상품 상세 페이지 | 상품 상세 HTML 반환           |
| /admin/products/{id}/edit   | GET  | 상품 수정 페이지 | 상품 수정 폼 HTML 반환         |
| /admin/products/{id}/edit   | POST | 상품 수정 처리  | 상품 수정 폼 HTML 데이터로 상품 수정 |
| /admin/products/{id}/delete | POST | 상품 삭제 처리  | 지정된 ID의 상품 삭제           |

- ***관리자 페이지 열기*** : http://localhost:8080/admin/products

### 회원 API

| URL                   | 메서드    | 기능       | 설명                                                |
|-----------------------|--------|----------|---------------------------------------------------|
| /api/members/register | POST   | 회원가입     | 이메일과 비밀번호, 유형을 입력받아서 중복된 이메일이 아닌 경우 회원 등록 및 토큰 반환 |
| /api/members/login    | POST   | 로그인      | 저장된 정보와 동일한 경우 토큰 반환                              |
| /api/members          | PUT    | 회원 정보 수정 | 이메일, 비밀번호, 유형, 토큰을 입력받아 인증 후 비밀번호 업데이트 및 토큰 반환    |
| /api/members/{email}  | DELETE | 회원 삭제    | 이메일과 토큰을 입력받아 인증 후 회원 삭제                          |
| /api/members/{email}  | GET    | 특정 회원 조회 | 이메일과 토큰을 입력받아 인증 후 특정 회원 정보 반환                    |
| /api/members          | GET    | 전체 회원 조회 | 토큰을 입력받아 인증 후 모든 회원 정보 반환                         |




