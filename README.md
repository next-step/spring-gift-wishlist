# 📦 상품 API

모든 HTTP 요청과 응답은 **JSON 형식**을 따릅니다.

---

<details>
<summary>🔎 상품 조회 (전체 상품)</summary>

### Request

```json
GET /api/products HTTP/1.1
```

### Response

```json
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "name": "아이스 카페 아메리카노 T",
        "price": 4500,
        "imageUrl": "https://st.kakaocdn.net/product/api/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
    },
    {
        "id": 2,
        "name": "(ICE)아메리카노",
        "price": 2000,
        "imageUrl": "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220622112804_d176787353ab48c690936557eefad11c.jpg"
    }
]
```

</details>
<details>
<summary>🔎 상품 조회 (특정 상품)</summary>

### Request

```json
GET /api/products/{productId} HTTP/1.1
```

### Response

```json
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "name": "아이스 카페 아메리카노 T",
    "price": 4500,
    "imageUrl": "https://st.kakaocdn.net/product/api/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
}
```

</details>
<details>
<summary>➕ 상품 추가</summary>

### Request

```json
POST /api/products HTTP/1.1
Content-Type: application/json

{
    "name": "(ICE)아메리카노",
    "price": 2000,
    "imageUrl": "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220622112804_d176787353ab48c690936557eefad11c.jpg"
}
```

### Response

```json
HTTP/1.1 201 Created
Content-Type: application/json

{
    "id": 1,
    "name": "(ICE)아케리카노",
    "price": 2000,
    "imageUrl": "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220622112804_d176787353ab48c690936557eefad11c.jpg"
}
```

</details>
<details>
<summary>✏️ 상품 수정</summary>

### Request

```json
PUT /api/products/{productId} HTTP/1.1
Content-Type: application/json

{
    "name": "[EVENT](ICE)아메리카노",
    "price": 1600,
    "imageUrl": "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20250515110714_9664acdff2b84e4e806c4d7d55dd8de0.jpg"
}
```

### Response

```json
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "name": "[EVENT](ICE)아메리카노",
    "price": 1600,
    "imageUrl": "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20250515110714_9664acdff2b84e4e806c4d7d55dd8de0.jpg"
}
```

</details>
<details>
<summary>❌ 상품 삭제</summary>

### Request

```json
DELETE /api/products/{productId} HTTP/1.1
```

### Response

```json
HTTP/1.1 204 No Content
```

</details>

# 🧑‍💻 관리자 화면

---

<details>
<summary>🔎 상품 조회</summary>

### 전체 상품 목록

[GET] http://localhost:8080/admin/products  
→ 등록된 모든 상품을 목록으로 확인할 수 있는 화면입니다.

### 특정 상품 조회

[GET] http://localhost:8080/admin/products/{productId}  
→ 선택한 상품의 상세 정보를 확인할 수 있는 화면입니다.
</details>
<details>
<summary>➕ 상품 추가</summary>

### 상품 추가 화면

[GET] http://localhost:8080/admin/products/new  
→ 새 상품을 입력하는 폼으로 이동합니다.

### 상품 추가 요청

[POST] http://localhost:8080/admin/products  
→ 폼에서 입력된 내용을 서버에 전송해 새 상품을 추가합니다.
</details>
<details>
<summary>✏️ 상품 수정</summary>

### 상품 수정 화면

[GET] http://localhost:8080/admin/products/edit/{productId}  
→ 선택한 상품의 정보를 수정할 수 있는 화면입니다.

### 상품 수정 요청

[PUT] http://localhost:8080/admin/products/{productId}  
→ HTML `<form>`에서 `_method=put`로 전송되는 요청입니다.  
→ 실제 HTTP 메서드는 `POST`이며,  
→ AdminController에서 `@PutMapping`으로 처리합니다.
</details>
<details>
<summary>❌ 상품 삭제</summary>

### 상품 삭제 요청

[DELETE] http://localhost:8080/admin/products/{productId}  
→ HTML `<form>`에서 `_method=delete`로 전송됩니다.  
→ 실제 HTTP 메서드는 `POST`이며,  
→ AdminController에서 `@DeleteMapping`으로 처리합니다.
</details>

# 💾 데이터베이스

---

<details>
<summary>🛠️ 사용 DB</summary>

### H2 Database (인메모리 DB)

- JDBC URL: `jdbc:h2:mem:spring-gift`
- Username: `sa`
- Password: ``

</details>
<details>
<summary>📌 DB 초기화</summary>

```sql
create table product
(
    id        bigint auto_increment primary key,
    name      varchar(255) not null,
    price     bigint       not null,
    image_url varchar(1000)
);
```

</details>

# ⚖️ 유효성 검사 및 예외 처리

---

<details>
<summary>🔍 유효성 검사</summary>

### 상품 이름

- 필수 입력
- 최소 1자, 최대 15자
- (), [], +, -, &, /, _ 외의 특수 문자를 사용할 수 없음
- RequiresApprovalWords 어노테이션을 사용하여 특정 단어가 포함되지 않도록 검사

### 상품 가격

- 0원 이상

### 상품 이미지 URL

- 필수 입력

</details>
<details>
<summary>🚨 예외 처리</summary>

### 상품 조회

- 상품이 존재하지 않을 경우: `ProductNotFoundException`

### 상품 수정

- 상품이 존재하지 않을 경우: `ProductNotFoundException`

### 상품 삭제

- 상품이 존재하지 않을 경우: `ProductNotFoundException`

</details>

# 🧪 테스트

---

<details>
<summary>API 테스트</summary>

- 상품 조회 (전체 상품)
- 상품 조회 (특정 상품)
- 상품 추가
- 상품 수정
- 상품 삭제

</details>
<details>
<summary>유효성 검사 테스트</summary>

- 상품 이름 (최대 15자 실패)
- 상품 이름 (특수 문자 성공)
- 상품 이름 (특수 문자 실패)
- 상품 이름 (MD 승인 글자)
- 상품 가격 (0원 이상 실패)