# spring-gift-wishlist

## step1 구현 기능
- dto에 대한 유효성 검증 도입 (name, price, quantity)
- GlobalExceptionHandler에서 유효성 검증 예외 처리
- 테스트 코드를 작성하여 예외 처리가 올바르게 되는지 확인

## step2 구현 기능
- jwt 의존성 추가
- 사용자 정보를 담을 유저 테이블 생성
- 로그인 인증을 위한 filter 생성
- 토큰을 발급하는 jwtTokenProvider 생성
- 토큰을 매 요청마다 검증하는 jwtAuthenticationFilter 생성
- 최초 관리자를 생성하고 관리자 권한 부여하는 API 생성
- 상품 생성 dto에 "카카오"라는 단어가 들어오면 Role이 관리자일 경우에만 가능 

## step3 구현 기능
- 비밀번호 저장 암호화 로직 추가
- 위시리스트 테이블 추가 및 기본 세팅
- ArgumentResolver를 사용한 어노테이션 기반 유저 주입
- 위시리스트 상품 추가 구현
- 위시리스트 상품 목록 조회 구현
- 위시리스트 상품 삭제 구현
- 위시리스트 e2e 테스트 코드 작성