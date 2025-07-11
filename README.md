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

# step2
- 사용자가 회원 가입, 로그인, 추후 회원별 기능을 이용할 수 있도록 구현
  - 비회원은 이메일과 비밀번호를 입력해 가입할 수 있다, 정상 가입이 되면 JWT 토큰을 반환한다
    - Request: 'POST /api/members/register'
      {
        "email":"testUser2@asdasd.asd",
        "password":"asd"
      }
    - Response: 201 Created
      {
      "token": "eyJhbGciOiJIUzI ... (이하 생략)"
    }
  - 회원은 이메일과 비밀번호를 입력해 로그인 할 수 있다, 정상 로그인이 되면 JWT 토큰을 반환한다
    - Request: 'POST /api/members/login'
      {
      "email":"testUser2@asdasd.asd",
      "password":"asd"
      }
    - Response: 200 OK
      {
      "token": "eyJhbGciOiJIUzI ... (이하 생략)"
      }
  - 회원은 이메일, 기존 비밀번호, 신규 비밀번호를 입력해 비밀번호를 변경 할 수 있다
    - Request: 'PATCH /api/members'
      {
      "email":"testUser2@asdasd.asd",
      "oldPassword":"asd",
      "newPassword":"asdasdasd"
    }
    - Response: 204 NO_CONTENT
  - 회원은 이메일, 비밀번호를 입력해 탈퇴할 수 있다
    - Request: 'DELETE /api/members'
      {
      "email":"testUser2@asdasd.asd",
      "password":"asdasdasd"
      }
    - Response: 204 NO_CONTENT
- 이제 회원만 상품 등록, 수정, 삭제가 가능하다
  - 상품의 전체 조회, 개별 조회는 비회원(토큰인증이 되지 않은)도 가능하다
  - 회원은 로그인 시 받은 토큰을 헤더의 Authorization키의 값으로 기입해 인증한다
  - "카카오" 가 들어간 상품 이름은 role이 'admin'인 회원만 가능하다

# step2 피드백 반영
- admin 페이지 권한 체크
  - 토큰을 클라이언트에 body로 전달은 하였으나 클라이언트로 하여금 받은 토큰을 헤더에 자동으로 넣게 하는 방법이 있는지 모르겠습니다
  - 일단 권한 체크 구현은 쿠키를 활용해 구현했으나 api에서 헤더를 통해 토큰을 전달 받는 것과 다르고 이로 인해 결국엔 코드 중복이 발생했습니다
  - 이런 상황에서는 어떤 방식으로 접근하는 것이 좋은지 질문 드리고 싶습니다
- 토큰 관련 기능 별도 클래스로 분리 완료
- 코드 중복 해결
  - 메서드로 분리
  - HandlerMethodArgumentResolver 활용 커스텀 어노테이션으로 공통 로그인 로직 분리

# step3
- 사용자는 위시를 등록할 수 있다
  - 회원은 각 상품별로 위시를 등록할 수 있다
    - Request: 'POST /api/wishes'
      {
      "productId":3,
      "quantity":1
      }
    - Response: 201 CREATED
      {
      "productResponseDto": {
      "id": 3,
      "name": "식빵",
      "price": 5000,
      "imageUrl": "abcdef"
      },
      "quantity": 1
      }
  - 회원은 자신의 위시리스트를 조회할 수 있다
    - Request: 'GET /api/wishes'
    - Response: 200 OK
      [
      {
      "productResponseDto": {
      "id": 3,
      "name": "식빵",
      "price": 5000,
      "imageUrl": "abcdef"
      },
      "quantity": 1
      },
      {
      "productResponseDto": {
      "id": 2,
      "name": "붕어빵",
      "price": 1000,
      "imageUrl": "abcdef"
      },
      "quantity": 2
      }
      ]
  - 회원은 위시의 수량을 변경할 수 있다
    - Request: 'PATCH /api/wishes/{productId}'
      {
      "quantity":1
      }
    - Response: 200 OK
      {
      "productResponseDto": {
      "id": 3,
      "name": "식빵",
      "price": 5000,
      "imageUrl": "abcdef"
      },
      "quantity": 1
      }
  - 회원은 위시를 삭제할 수 있다
    - Request: 'DELETE /api/wishes/{productId}'
    - Response: 204 NO_CONTENT
- 로그인 시 사용자 검증 인터셉터로 이동