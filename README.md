# spring-gift-wishlist

### step0: 상품관리 코드 이전
- 위시 리스트 개발 전 상품 관리 코드 이전으로 환경 세팅 완료

### step1: 유효성 검사 및 예외 처리
- 상품 이름
  - 필수 입력이다.
  - 공백 포함 최대 15자까지 입력 가능하다.
  - 일부 특수문자만 허용한다. ex) (), [], +, -, &, /, _
  - "카카오"라는 문구가 들어간 경우 담당 MD와 협의한 경우에만 사용 가능하다.
- 가격
  - 필수 입력이다.
  - 0원 이상의 값을 가진다.
- 이미지 URL
  - 필수 입력이다.
  - 유효한 형식의 URL을 입력해야 한다.

### step2: 회원 로그인
- 회원가입 구현
  - 회원은 이메일과 비밀번호 입력을 통해 가입할 수 있다.
  - 회원 로그인 API 구현 목록
    - 회원가입: `POST /api/members/register`
    - 로그인: `POST /api/members/login`
- 토큰 발급
  - 토큰을 발급 받으려면 이메일과 비밀번호를 보내야 한다.
  - 이후 가입한 이메일과 비밀번호가 일치하면 토큰이 발급된다.
- 회원을 조회, 추가, 수정, 삭제할 수 있는 관리자 화면을 구현한다.
  - 관리자 멤버 관리 API 구현 목록
    - 멤버 전체 조회: `GET /api/members`
    - 멤버 단건 조회: `GET /api/members/{id}`
    - 멤버 생성: `POST /api/member`
    - 멤버 수정: `PUT /api/member/{id}` 
    - 멤버 삭제: `DELETE /api/member{id}`
  - 관리자 멤버 관리 페이지
    - 멤버 목록 페이지: `http://localhost:8080/admin/members`
    - 멤버 추가 페이지: `http://localhost:8080/admin/members/new`
    - 멤버 수정 페이지: `http://localhost:8080/admin/members/{id}/edit`