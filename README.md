# spring-gift-wishlist

## 위시 리스트

### 🚀 0단계 - 기본 코드 준비

- [x] 상품 관리 코드를 옮기기

### 🚀 1단계 - 유효성 검사 및 예외 처리

상품을 추가하거나 수정하는 경우, 클라이언트로부터 잘못된 값이 전달될 수 있다.
잘못된 값이 전달되면 클라이언트가 어떤 부분이 왜 잘못되었는지 인지할 수 있도록 응답을 제공한다.

- [ ] 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
- [ ] 특수 문자
    - 가능: ( ), [ ], +, -, &, /, _
    - 그 외 특수 문자 사용 불가
- [ ] "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.

#### 🛠 구현할 기능 목록

- [ ] E2E 테스트 코드 작성하기(CRUD)

- [ ] 상품 추가 API
    - [ ] 단건 상품 추가
        - [ ] **Request**: POST /api/products
          ```json
          {
              "name": "아이스 카페 아메리카노 T",
              "price": 4500,
              "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
          }
          ```
        - [ ] **예외**:
            - name, price, imageUrl 중 하나라도 값이 존재하지 않을 때: 400 Bad Request

- [ ] 상품 조회 API
    - [ ] 전체 상품 조회
        - [ ] **Request**: GET /api/products

    - [ ] 단건 상품 조회
        - **Request**: GET /api/products/{productId}
        - [ ] **예외**:
            - 데이터베이스에 productId가 존재하지 않을 때: 404 Not Found

- [ ] 상품 수정 API
    - [ ] 단건 상품 전체 수정
        - [ ] **Request**: PUT /api/products/{productId}
          ```json
          {
              "name": "아이스 카페 아메리카노 T",
              "price": 9000,
              "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
          }
          ```
        - [ ] **예외**:
            - 데이터베이스에 productId가 존재하지 않을 때: 404 Not Found
            - name, price, imageUrl 중 하나라도 존재하지 않을 때: 400 Bad Request

- [ ] 상품 삭제 API
    - [ ] 단건 상품 삭제
        - [ ] **Request**: DELETE /api/products/{productId}
        - [ ] **예외**:
            - 데이터베이스에 productId가 존재하지 않을 때: 404 Not Found