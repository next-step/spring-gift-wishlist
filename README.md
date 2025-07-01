# spring-gift-wishlist

| method | url                | 기능       |
|--------|--------------------|----------|
| GET    | /api/products      | 상품 전체 조회 |
| POST   | /api/products      | 상품 생성    |
| PATCH  | /api/products/{id} | 상품 수정    |
| DELETE | /api/products/{id} | 상품 삭제    |
| GET    | /admin             | 관리자 페이지  |

step 0. 기본 코드 준비

1. 1주차 코드를 그대로 가져오자


step 1. 유효성 검사 및 예외 처리

기능 요구 사항
1. 상품이름은 공백을 포함하여 최대 15자까지 입력 가능(특수문자는 (),[],+,-,&,/,_ 를 제외한 나머지는 사용불가)
2. 상품이름에 "카카오" 가 포함된 문구는 담당 MD와 협의한 경우에만 가능하게 구현
