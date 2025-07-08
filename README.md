#spring-gift-wishlist
## 0단계 기본 코드 준비
## 1단계 유효성 검사 및 예외 처리
상품을 추가하거나 수정하는 경우
잘못된 값이 전달되면 클라이언트가 어떤 부분이 왜 잘못되었는지 인지할 수 있도록 응답을 제공한다.

1. 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
2. 특수 문자 가능: ( ), [ ], +, -, &, /, _(그 외에는 불가)
3. "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.


# spring-gift-product
## 1단계

1. 상품 추가 기능
POST /api/products

2. 특정 상품 조회 기능
GET /api/products/{productId}

3. 모든 상품 조회 기능
상품 목록 조회 (페이지네이션 적용)	모든 상품의 목록을 페이지 단위로 조회한다(추후 예정)
GET	/api/products?page=0&size=10&sort=name,asc&categoryId=1	

4. 상품 수정 기능
PUT	/api/products/{productId}

5. 상품 삭제 기능
DELETE /api/products/{productId}

## 2단계

1. 관리자 상품 추가 기능

2. 관리자 모든 상품 조회 기능
상품 목록 조회 (페이지네이션 적용)	모든 상품의 목록을 페이지 단위로 조회한다(추후 예정)

3. 관리자 상품 수정 기능

4. 관리자 상품 삭제 기능

## 3단계

1. h2 데이터베이스 환경설정

    - build.gradle에 h2 데이터베이스 의존성 추가
    - application.yml에 h2 데이터베이스 설정 추가
      - H2 콘솔 활성화
      - JDBC URL,username,password 설정

2. 스키마 스크립트, 데이터 스크립트 생성

    - src/main/resources/schema.sql

3. CREATE 기능 구현(JdbcClient)

4. READ 기능 구현(JdbcClient)

5. UPDATE 기능 구현(JdbcClient)

6. DELETE 기능 구현(JdbcClient)

# spring-gift-wishlist

## 1단계

1. 유효성 검사 및 에러처리
   - 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
   - 특수 문자 가능: ( ), [ ], +, -, &, /, _(그 외에는 불가)
   - "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.