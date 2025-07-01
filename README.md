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