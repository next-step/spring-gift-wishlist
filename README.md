# spring-gift-wishlist

## Step 1. 유효성 검사 및 예외 처리
1. spring-boot-starter-validation 의존성 추가
2. AddRequestDto에 대해 Validation 추가, test code 작성
3. ModifyRequestDto에 대해 Validation 추가, test code 작성
4. readme.md 작성 완료 및 pr 전송

### Validation 적용 여부
1. 이름에 대하여 Validation 적용
- 허용되지 않은 특수문자 금지
- 1-15자의 길이 제한
- 이름에 "카카오"가 들어갈 경우 MD와의 협의 여부에 따라 승인해야하므로, RequestDto에 MD와의 협의 여부를 필수로 포함하도록 수정
2. Validation 및 코드 내에서 throw되는 Exception에 대하여 Custom Exception 선언 및 적용
- ExceptionHandler를 통해 에러 컨트롤
- ResponseEntity<String>을 통해 문제점이 되는 부분들을 명시 및 BAD_REQUEST 반환
3. Test Code 작성
- 상품 추가, 조회, 수정, 삭제에 대해 Test Code 작성 완료
- Step 진행하면서 미비한 케이스 더 추가할 예정

## Step 2. 회원 로그인
1. step1에서 리뷰 받은 부분에 대해 적절한 수정 진행
2. member entity 생성
3. member와 관련한 controller, service, repository 생성
4. 회원가입 구현
5. 로그인 구현

| api                   | method | 기능                    | 완료여부 |
|-----------------------|--------|-----------------------|------|
| /api/members/register | POST   | 회원가입(request body 요구) | 완료   |
| /api/members/login    | POST   | 로그인(request body 요구)  | 완료   |

### 일부 의도한 사항 정리
1. 회원가입은 전부 USER로 처리된다.
- 이는 의도된 사항으로 api를 통해 회원가입 신청이 올 경우 일반 유저일 것이라는 가정하에 제작했다.
- ADMIN 회원가입은 개발자가 DB에 끼워넣을 수도 있는 것이기에, 억지로 구현하지 않았다.
2. 로그인 시 역할도 함께 포함하여 token화 했다.
- 추후 권한 관련 구현 시 토큰 해독을 통해 역할을 확인하여 권한을 제한 할 수 있다.

## Step 3.  위시 리스트
1. step 3를 위한 wishlist table 생성
2. Dto 선언 및 wishlist 상품 추가 구현
3. wishlist 상품 조회 구현
4. wishlist 상품 삭제 구현
4. wishlist 상품 개수 수정 구현 (0으로 입력할 경우 삭제)

| api                       | method | 기능                                         | 완료여부 |
|---------------------------|--------|--------------------------------------------|----|
| /api/wishlist             | GET    | 본인의 wishlist 목록 불러오기 (header 필수)           | 완료 |
| /api/wishlist             | POST   | wishlist에 물건 추가 (header, body 필수)          | 완료 |
| /api/wishlist/{productId} | DELETE | wishlist에 담겨 있는 물품 삭제 (header 필수)          | 완료 |
| /api/wishlist             | PATCH | wishlist에 담겨 있는 물품 개수 수정 (header, body 필수) | 완료 |

### 구현간 특이사항 정리
1. wishlist table은 member와 product table의 id에 의존하는 table로 구성(foreign key 활용)
2. 