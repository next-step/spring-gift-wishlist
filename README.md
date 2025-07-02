# spring-gift-wishlist

## 1단계 코드리뷰요청 (구현내용 명세)
### 입력내용 검증
- 상품명, 상품가격, 상품이미지URL 검증 및 검증실패 메시지 명시
  - 상품명 검증내용 : 존재여부, 길이(과제 제시 15자), 입력가능문자
  - 상품가격 검증내용 : 존재여부, 양수여부
  - 상품이미지URL : 존재여부, 길이(DB 정의 255자)
- `thymeleaf(/admin/products ...)`를 통해 접근하는 경우의 에러메시지 출력
- `api(/api/products ...)`를 통해 접근하는 경우의 에러메시지 출력
- 상품명에 '카카오'가 포함되어있는지 확인 후, 포함되어있다면 `validated=false`로 설정하여 일반 사용자는 조회 불가하도록 하는 기능
  - 상품명에 '카카오'가 포함되더라도, **상품 등록은 성공**하나 `thymeleaf`에서는 별도 안내메시지 출력되며 `api`에서는 validated 값을 보고 알도록 함
- `/admin/products/{id}`를 통해 접속하여 `validated=true`로 설정하는 기능
### 테스트코드 작성
- `ProductController`에 대한 테스트코드 작성
- `AdminPageController`에 대한 테스트코드 작성