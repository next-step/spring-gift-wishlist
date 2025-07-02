# spring-gift-wishlist
> 유효성 검사 및 예외 처리
## 기능 요구 사항
* Validation 고도화
  * 상품 명 15자까지 제한
  * 특수 문자
    * 가능: (), [], +, -, &, /, _ 
    * 그 외 불가능
* "카카오"가 포함된 문구는 당담 MD와 협의 후 사용 가능
  * Status enum 구현
  * Product에 Status 추가 - [APPROVED, REJECTED, PENDING]
  * Service, Respository에 Status 수정 로직 구현
  * Product에 Status를 수정할 수 있는 Endpoint 구현
  * GET api/products 에선 APPROVED인 Product만 리턴하도록 수정
