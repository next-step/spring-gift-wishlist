# spring-gift-wishlist

### Step0 [ 07/01 ]

- [x] README 파일 작성
- [x] Merge된 1단계 기능 Test
  - [x] 상품관리 API 테스트
  - [x] 관리자 API 테스트

### Step1 [ 07/ 02 ]
- [x] 상품 이름 크기 제한(15자)
- [x] 특수문자 제한
- [x] "카카오" 문구 제한
- [x] Test Code 추가

### Step1 Refactoring [ 07/04 ]
- [x] 필요없는 코드들 삭제
- [x] 이름 검증 메소드 service -> Entity로
- [x] 가격 음수 입력 제한
- [x] Validation 위반시 status code 수정
- [x] 다양한 테스트 케이스 추가

### Step2 [ 07/07 ]
- [x] 사용자 회원가입 구현
- [x] 사용자 로그인 구현
  - [x] token이 잘못된 경우 401 Error 발생
  - [x] 이메일/비밀번호가 잘못된 경우 403 Error 발생
- [ ] 사용자 관리 페이지 구현
  - [x] 회원 조회기능
  - [ ] 회원 수정기능
  - [ ] 회원 삭제기능

### Step2 Refactoring [ 07/08 ]
- [x] Spring Interceptor 기반 인증체계 구축
- [x] 회원가입 페이지 생성
- [x] 로그인 페이지 생성
- [ ] 회원가입에 대한 테스트 케이스 추가
  - [x] 회원 생성
  - [ ] 회원 수정
  - [ ] 회원 삭제

### Step2 Refactoring [ 07/09 ]
- [x] token 생성 비밀키 property값으로 변경
- [x] Member Field에 대한 Validation 설정
  - [x] 비밀번호는 대소문자/특수문자/숫자 포함
  - [x] Email형식 제한
- [x] 로그인 실패시 Http Status UnAuthorized로
- [x] 로그인 테스트 케이스 추가
- [x] 회원가입 성공시 Http Status 201 반환하도록

### Step3 [ 07/10 ]
- [x] MemberService에서 Http Header에 token 붙여 반환
- [ ] 위시리스트 기능 구현
  - [x] 위시리스트 페이지 구현
  - [x] 상품 목록 조회
  - [x] 상품 추가
  - [ ] 상품 수량 조절
  - [ ] 상품 삭제