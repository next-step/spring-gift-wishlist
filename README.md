# spring-gift-wishlist

# step0
- spring-gift-product에서 복사 완료

# step1
- 상품을 추가하거나 수정하는 경우 클라이언트로부터 잘못된 값이 전달되면 에러 응답을 제공한다
  - CreateProductRequestDto에 Bean Validation을 적용
    - '( ), [ ], +, -, &, /, _'을 제외한 특수문자 입력 불가능
      {
      "httpStatus": "BAD_REQUEST",
      "message": "( ), [ ], +, -, &, /, _ 외의 특수 문자는 사용 불가능 합니다"
      }
    - 상품 이름은 공백을 포함애 최대 15자 까지 가능
      {
      "httpStatus": "BAD_REQUEST",
      "message": "최대 15자 까지 입력 가능합니다"
      }
    - 상품의 가격은 0을 포함한 양수만 입력 가능 (음수 불가능)
      {
      "httpStatus": "BAD_REQUEST",
      "message": "가격은 음수가 될 수 없습니다"
      }
  - '카카오'가 포함된 문구가 이름 입력에 존재 할 시 담당 MD와 협의한 경우에만 사용가능
    - 데이터 베이스에 저장은 되나 get 메서드로 읽기가 불가능하다 (id로 개별 조회 시 에러 메시지 반환)
      {
      "httpStatus": "FORBIDDEN",
      "message": "개시 중단됨, 개시를 원하시면 담당 MD와 협의해 주세요"
      }
    - 담당 MD가 patch 메서드로 승인을 해주면 상품이 조회가 가능하다 (카카오 이름이 없으면 자동 승인) 

# step1 피드백 반영
- 코드 포맷 수정
  - 불필요하게 들어간 줄바꿈 제거 
  - CreateProductRequestDto 줄바꿈 추가
- getProductRowMapper 메서드 대신 변수로 선언
- '카카오'가 포함된 문구가 이름 입력에 존재 할 시 담당 MD와 협의한 경우에만 사용가능 구현 변경
  - Bean Validation을 적용해 클라이언트로부터 이름에 '카카오' 단어가 포함된 문자열이 전달되면 잘못된 요청으로 예외처리 됨
    {"httpStatus":"BAD_REQUEST","message":"'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다"}
