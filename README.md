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
  - [ ] 회원 생성
  - [ ] 회원 수정
  - [ ] 회원 삭제