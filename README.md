# spring-gift-product
***
## Step 1 : API 명세(RestController)
| 기능    | Method | URL                       |request|response| 상태코드     |
|-------|-------|---------------------------|----|----|----------|
| 상품 조회 | GET| /api/products/{productId} |요청 param|상품 정보| 200:정상조회 |
| 상품 추가 |POST| /api/products             |요청 body|등록 정보| 201:정상추가 |
| 상품 수정 |PUT| /api/products/{productId} |요청 body|수정 정보| 200:정상수정 |
| 상품 삭제 |DELETE| /api/products/{productId} |요청 param|X| 204:정상삭제 |

## Step 2 : View Controller 명세
| 기능    | Method | URL            |응답 페이지   |
|-------|-------|------------------|----------|
| 상품 목록 조회 | GET| / || index.html|
| 상품 단건 조회 |GET| /products/{id} |detail.html |
| 상품 추가 |GET+POST| /product/news + /products      |form.html + redirect:/|
| 상품 수정 |GET+POST| /products/{id} |update.html + redirect:/products/{id} |
| 상품 삭제 |GET| /products/{id}/delete | redirect:/ |

## Step 3 : H2 DB
- H2 DB 사용 설정
- Product schema 및 data.sql 설정
- 상품정보 : 자바 컬렉션 기반 -> H2 DB (Repository 구현)

# spring-gift-wishlist

## Step 1 : 유효성 검사 및 예외 처리
### 요구사항 분석
- 상품을 추가하거나 수정하는 경우, 클라이언트로부터 잘못된 값이 전달될 수 있다. 
  잘못된 값이 전달되면 클라이언트가 어떤 부분이 왜 잘못되었는지 인지할 수 있도록 응답을 제공한다.
- 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
- 특수 문자 검증
- "카카오"가 포함된 문구 검증
### 구현 방향  
- Service layer의 create와 update 로직에서 `try-catch`문을 중복하여 사용하지말고, `@ExceptionalHandler` 사용
- 상품 이름은 `@NotBlank`로 가장 엄격하게 입력값을 검증함과 동시에 `@Size`를 이용
- `@Pattern`과 `regex`를 이용
- `Custom Validator` 이용