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