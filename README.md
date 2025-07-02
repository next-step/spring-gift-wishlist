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
### Boolean과 getter, setter
- boolean은 true, false 밖에 가지지 않아 getter 이름으로 isOO() 형이 가능
- Boolean은 여기에 null도 가능하고, 가급적 getOO() 형의 이름을 사용 권장
- setter의 경우 둘다 setOO 사용 권장
- 이 때문에 intellij에서 Boolean에 대한 getter와 setter의 인식에 문제가 생길 수가 있으므로, 네이밍에 항상 주의!
- 인식 문제가 생긴다고 해서 동작 문제가 생기지는 않음
- 다만 혼란 방지를 위해 @JsonProperty 어노테이션을 붙여주면 intellij에서도 인식을 잘 한다!
- Json과 연결되는 Dto가 아닌 경우 @SupressWarning 어노테이션을 활용할 수도 있다
2. 미션이 뭐지...?

