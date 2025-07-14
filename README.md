# 기능 요구 사항
이전 단계에서 로그인 후 받은 토큰을 사용하여 사용자별 위시 리스트 기능을 구현한다.

위시 리스트에 등록된 상품 목록을 조회할 수 있다.
위시 리스트에 상품을 추가할 수 있다.
위시 리스트에 담긴 상품을 삭제/수량 변경

# 실행 결과
사용자 정보는 요청 헤더의 Authorization 필드를 사용한다.

- Authorization: <유형> <자격증명>

  ```Authorization: Bearer token```

## 사용자 시나리오

### 위시 리스트 상품 추가
1. 클라이언트가 상품 목록을 조회 (GET /api/products)
2. 서버가 상품 목록을 반환 (200 OK)
3. 클라이언트가 위시리스트에 상품 A를 추가 (POST /wishlist)
4. 서버가 해당 사용자의 위시리스트에 상품 추가 (201 Created)
5. 성공 응답 반환

### 위시리스트 상품 삭제
1. 사용자가 위시리스트 목록을 조회 (GET /wishlist)
2. 서버가 위시리스트 목록을 반환 (200 OK)
3. 사용자가 목록에서 특정 상품의 삭제 버튼을 클릭 (DELETE /wishlist/{id})
4. 서버가 해당 상품을 위시리스트에서 삭제 (204 No Content)
5. 성공 응답 반환

### 위시리스트 수량 변경
1. 사용자가 위시리스트 목록을 조회 (GET /wishlist)
2. 서버가 위시리스트 목록을 반환 (200 OK)
3. 사용자가 특정 상품의 수량을 변경 (PUT /wishlist/{id})
4. 서버가 해당 상품의 수량을 업데이트 (200 OK)
5. 수정된 위시리스트 항목 반환

---
### 기능 정리
- 위시 리스트에 등록된 상품 목록을 조회할 수 있다.
  - GET /wishlist
- 위시 리스트에 상품을 추가할 수 있다.
  - POST /wishlist
- 위시 리스트에 담긴 상품을 삭제할 수 있다.
  - DELETE /wishlist/{id}
- 위시 리스트에 담긴 상품의 수량을 변경할 수 있다.
  - PUT /wishlist/{id}

---
### HandlerMethodArgumentResolver
- 컨트롤러 메서드의 파라미터를 처리하는 인터페이스 (전처리)
  ```public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    ...
            return new Member(1L, "test@email.com", "1234");
    }
  ``` 
  ```@PostMapping("/wishes")
  public void create(
      @RequestBody WishRequest request,
      @LoginMember Member member
  ) {
  }
  ```
---
# 단계별 구현 
## 1. 사용자 인증 기반 구조 설정
  - [x] JwtAuthFilter를 통해 요청 시 Authorization 헤더에서 토큰 추출
  - [x] @LoginMember 커스텀 어노테이션을 통해 Controller 메서드에서 로그인 사용자 주입
  - [x] 로그인한 사용자만 위시리스트 접근 가능하도록 보안 설정
## 2. 도메인 설계 (Wish)
  - [X] 필드: id, memberId, productId, quantity
  - [X] 연관관계: ManyToOne Member, ManyToOne Product
  - [X] DB 제약: 한 사용자가 같은 상품을 여러 번 등록하지 못하도록 (memberId + productId) 유니크 설정
## 3. 위시리스트 추가 (POST /wishlist)
  - [x] 로그인된 사용자의 memberId와 요청받은 productId, quantity로 Wish 생성
  - [x] 이미 등록된 상품일 경우 → 수량만 증가시키거나 예외 처리
  - [x] 성공 시 201 Created 또는 200 OK 응답
## 4. 위시리스트 조회 (GET /wishlist)
  - [ ] 로그인된 사용자의 위시리스트 전체 조회 (인증된 사용자만 접근 가능)
  - [ ] Wish 엔티티를 Product 정보와 함께 조인하여 응답 (해당 사용자의 Wish 리스트 조회)
  - [ ] 상품 정보 (이름, 이미지, 가격 등) 포함 응답 -> 성공 시 200 OK 응답
## 5. 위시리스트 수량 변경 (PUT /wishlist/{id})
  - [ ] 해당 ID의 Wish가 현재 로그인한 사용자 것인지 검증 
  - [ ] 요청된 수량으로 Wish.quantity 필드 업데이트
  - [ ] 수정된 Wish 목록 반환
## 6. 위시리스트 삭제 (DELETE /wishlist/{id})
  - [ ] 해당 ID의 Wish가 현재 로그인한 사용자 것인지 검증
  - [ ] Wish 삭제 후 성공 응답 (204 No Content)
  - [ ] 삭제된 Wish 목록 반환
## 7. 예외 처리
  - [ ] 인증되지 않은 사용자 → 401 Unauthorized 
  - [ ] 다른 사용자의 Wish 접근 시 → 403 Forbidden 
  - [ ] 존재하지 않는 상품, 잘못된 수량 요청 등 → 400 Bad Request

## API 명세

### 위시리스트 조회
- **GET** `/wishlist`
- **Authorization**: `Bearer <token>`
- **Response**: `200 OK` - 위시리스트 목록

### 위시리스트 상품 추가
- **POST** `/wishlist`
- **Authorization**: `Bearer <token>`
- **Request Body**: `{"productId": 1, "quantity": 2}`
- **Response**: `201 Created` - 추가된 위시리스트 항목

### 위시리스트 수량 변경
- **PUT** `/wishlist/{id}`
- **Authorization**: `Bearer <token>`
- **Request Body**: `{"quantity": 3}`
- **Response**: `200 OK` - 수정된 위시리스트 항목

### 위시리스트 상품 삭제
- **DELETE** `/wishlist/{id}`
- **Authorization**: `Bearer <token>`
- **Response**: `204 No Content`

## 데이터 모델

### Wish 엔티티
```java
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"memberId", "productId"})
})
public class Wish {
    private Long id;
    private Long memberId;  // 사용자별 구분을 위한 필드
    private Long productId;
    private Integer quantity;
}
```

### WishRequest DTO
```java
public class WishRequest {
    private Long productId;
    private Integer quantity;
}
```

### WishResponse DTO
```java
public class WishResponse {
    private Long id;
    private ProductResponse product;
    private Integer quantity;
}
```

## 예외 처리 시나리오

### 인증 실패
- **상황**: 토큰이 없거나 유효하지 않음
- **응답**: `401 Unauthorized`
- **메시지**: "인증이 필요합니다."

### 권한 없음
- **상황**: 다른 사용자의 위시리스트 접근
- **응답**: `403 Forbidden`
- **메시지**: "권한이 없습니다."

### 잘못된 요청
- **상황**: 존재하지 않는 상품 ID, 음수 수량 등
- **응답**: `400 Bad Request`
- **메시지**: "잘못된 요청입니다."

### 중복 상품 추가
- **상황**: 이미 위시리스트에 있는 상품 추가
- **응답**: `409 Conflict`
- **메시지**: "이미 위시리스트에 추가된 상품입니다."

## 위시리스트 조회 응답 예시
```json
{
  "wishes": [
    {
      "id": 1,
      "product": {
        "id": 1,
        "name": "상품A",
        "price": 10000,
        "imageUrl": "image1.jpg"
      },
      "quantity": 2
    }
  ]
}
```
현재 로그인한 사용자의 위시리스트만 반환하기 !!!
