# spring-gift-wishlist

## Step 0: 구현한 기능

* [X] 상품 관리 코드 이전

## Step 1: 구현한 기능

* [X] 상품 이름 유효성 검사: 공백 포함 최대 15자
* [X] 특수 문자 유효성 검사: (), [], +, -, &, /, _
* [X] 특수 문자열 예외 처리; '카카오' 가 포함된 문구

## Step 2: 구현한 기능

* [X] 도메인 & DB 설계
* [X] DAO 구현
* [X] 비밀번호 해싱
* [X] JWT 발급&검증
* [X] 인증 필터 & 예외 처리
* [X] 회원 서비스 레이어 구성
* [X] 컨트롤러 구현
* [X] (선택)관리자 화면
* [X] 테스트 코드 작성

## Step 3: 구현한 기능

* [ ] 로그인 사용자 식별
    * JWT 토큰에서 사용자 정보 추출
    * @LoginMember 리졸버 구현
* [ ] 위시리스트 API 구현
    * 조회: GET api/wishes
    * 추가: POST api/wishes + @RequestBody WishRequest
    * 수량 수정: PUT api/wishes/:id + @RequestBody WishRequest
    * 삭제: DELETE api/wishes/:id
* [ ] 테스트 코드 작성
