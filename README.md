# spring-gift-wishlist

## 상품 위시 리스트 - step 2: 로그인 및 회원가입 구현
- member 도메인을 추가한 뒤, 로그인/회원가입 및 JWT 기반 인증을 구현함.

### 1. 로그인
- LoginRequest dto를 통해 이메일/비밀번호 정보를 받음.
- db에 저장된 비밀번호와 일치하면 token을 발급해 줌.

### 2. 회원가입
- RegisterRequeset dto를 통해 이메일/비밀번호 정보를 받음.
- 새로운 멤버를 등록하고, 등록한 멤버의 memberId를 반환함.

### 3. JWT 기반 인증
- 이후 사용자는 로그인 시에 발급받은 토큰을 다른 API 요청 시에 헤더에 포함해서 요청해야 함.
- 토큰 없이 API를 요청한 사용자는 interceptor 단에서 요청이 거부된다.
