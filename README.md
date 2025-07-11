# spring-gift-wishlist

| method | url                  | 기능         |
|--------|----------------------|------------|
| GET    | /api/products        | 상품 전체 조회   |
| POST   | /api/products        | 상품 생성      |
| PATCH  | /api/products/{id}   | 상품 수정      |
| DELETE | /api/products/{id}   | 상품 삭제      |
| GET    | /admin               | 관리자 페이지    |
| POST   | /api/members/register | 회원가입       |
| POST   | /api/members/login   | 로그인        |
| GET    | /api/wish-list    | 위시리스트 조회   |
| POST   | /api/wish-list    | 위시리스트 상품등록 |
| DELETE   | /api/wish-list    | 위시리스트 상품삭제 |

step 0. 기본 코드 준비

1. 1주차 코드를 그대로 가져오자


step 1. 유효성 검사 및 예외 처리

- 이름 유효성 검사 조건
  1. 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
  2. 특수 문자는 (),[],+,-,&,/,_ 를 제외하고 사용할 수 없다
  3. "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.

1. 상품생성시 이름 유효성 검사
2. 상품수정시 이름 유효성 검사
3. 테스트코드 작성

step 1. 피드백
1. 가격에 양수만 받게 하기
2. 가격에 음수가 들어갔을때 테스트케이스 만들기
3. 제목에 "카카오"가 포함될수 있는상품 테스트케이스 만들기

step 2. 회원 로그인
1. 회원가입 api 구현
2. 로그인 api 구현
3. 토큰 구현


step 2. 피드백
1. 회원가입,로그인 테스트 코드 추가 
2. print()제거
3. 인증실패시 에러코드 : unauthorized 응답수정
4. login 메소드명 existMember로 변경
5. 토큰생성시 currentTimeMillis 값을 하나로 통합 하고 시간변경
6. ResponseEntity<?>로 받던걸 ResponseEntity<Dto>로 받게 변경
7. MemberService내에서 JWT토큰이 반환되도록 변경

step 3. 위시 리스트
- 기본조건
  - 로그인 후 받은 토큰을 사용하여 사용자별 위시 리스트 기능구현
1. 위시 리스트에 등록된 상품 목록 조회
2. 위시 리스트에 상품 추가
3. 위시 리스트에 상품 삭제
