# spring-gift-wishlist
# [step0] 기존 코드 불러오기

git으로 불러오려 했으나 여러 차례 실패하여 직접 복사하여 붙여넣는 방식으로 가져옴

---
# [step1] 유효성 검사 및 예외처리

**과제 요구 사항**: 상품명 입력에 대한 예외처리
- 공백 포함 최대 **15자**
- 특수문자: `()`, `[]`, `+`, `-`, `&`, `/`, `_` 만 사용 가능
- `"카카오"`가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능

---

## 구현 기능 목록
- [x] 상품명 유효성 검사(길이, 특수문자 제한, "카카오" 포함 제한)
- [x] 유효성 검사 실패 시, 클라이언트에 에러 메시지 응답
- [x] `@Valid`, `@ControllerAdvice` 기반 전역 예외 처리 구현
- [x] 커스텀 Validator를 통한 상품명 규칙 검사
- [x] 각 기능별 테스트 코드 작성 (점진적으로 진행)

## 구현 방법
1. 상품명 유효성 검사 
   - 길이 제한: @Size
   - 특수 문자 제한: @Pattern
   - "카카오" 포함 제한: 커스텀 어노테이션 @NoKakao

2. 클라이언트 에러 메시지 응답
- 전역 예외 처리기(`@ControllerAdvice`)에서 `MethodArgumentNotValidException`을 처리
- 응답 형식: `Map<String, List<String>>` 구조로 각 필드별 모든 에러 메시지 반환

---

## 테스트 예시

- 요청 데이터:

```json
{
  "name": " 0123456789카카오!!~",
  "price": -2000,
  "imageUrl": ""
}
```

- 응답 (HTTP 400 Bad Request):
```json
{
  "name": [
    "상품명에 '카카오'를 포함할 수 없습니다. 담당자에게 문의하세요.",
    "상품명에는 특수문자 (),[],+,-,&,/,_ 만 포함될 수 있습니다.",
    "상품명은 공백 포함 최대 15자까지 입력할 수 있습니다."
  ],
  "price": [
    "상품 가격은 0보다 큰 값으로 입력해주세요"
  ],
  "imageUrl": [
    "상품 이미지를 등록해주세요."
  ]
}
```
---
# [step2] 회원 로그인

## 구현 기능 목록
- [x] 회원가입
- [x] 로그인
- [x] JWT 토큰 발급 및 검증 기능
- [x] 인증 필터 구현(인증 실패 시 401, 로그인 실패 403)

## 구현 기능 
1. 회원가입 api 구현(`POST /api/members/register`)

### Request
```http
POST /api/members/register HTTP/1.1
Content-Type: application/json
Host: localhost:8080
```
```json
{
  "email": "test@email.com",
  "pwd": "abc1234"
}
```
### Response
```http
HTTP/1.1 201 Created
Location: /api/members/register/test@email.com
Content-Type: application/json
```
```json
{
   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJ0ZXN0QGVtYWlsLmNvbSJ9.7eR29_0..."
}
```
2. 로그인 api 구현(`POST /api/members/login`)

### Request
```http
POST /api/members/register HTTP/1.1
Content-Type: application/json
Host: localhost:8080
```
```json
{
  "email": "test@email.com",
  "pwd": "abc1234"
}
```
### Response(성공 시)
```http
HTTP/1.1 200 Ok
Content-Type: application/json
```
```json
{
   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJ0ZXN0QGVtYWlsLmNvbSJ9.7eR29_0Aj0oPzBZ7K7MPa1FwW79CtWZKHlDZXVB8izw"
}
```
### Response(실패 시: 잘못된 비밀번호 or 없는 계정)
```http
HTTP/1.1 403 Forbidden
Content-Type: application/json
```
```json
{
   "message": "회원이 존재하지 않습니다." // or "비밀번호가 일치하지 않습니다."
}
```
---
# [step3] 위시 리스트

## 구현 기능 목록
- [x] 위시리스트에 상품 추가
- [ ] 위시리스트에서 상품 삭제
- [x] 위시리스트에 담긴 상품 목록 조회
- [ ] 위시리스트 테스트 

## 구현 기능
1. 위시리스트에 상품 추가(`POST /api/wishes`)

### Request
```http
Authorization: Bearer {access_token}
Content-Type: application/json
```
```json
{
   "productId": 1
}
```
### Response
```http
HTTP/1.1 201 Created
Location: /api/wishes/{wishId}
Content-Type: application/json
```
```json
{
   "message": "위시리스트에 추가되었습니다."
}
```
응답코드
- 201 Created: 성공적으로 추가됨
- 401 Unauthorized: 인증 실패 (토큰 누락/오류)
- 404 Not Found: 존재하지 않는 상품

2. 위시리스트 상품 조회(`GET /api/wishes`)
### Request
```http
Authorization: Bearer {access_token}
```

### Response
```json
[
   {
      "id": 1,
      "productId": 1,
      "productName": "아이스 카페 아메리카노 T",
      "price": 4700,
      "imageUrl": "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg"
   },
   {
      "id": 2,
      "productId": 3,
      "productName": "배민상품권 2만원 교환권",
      "price": 20000,
      "imageUrl": "https://st.kakaocdn.net/product/gift/product/20230830170233_21660381ee6d4c06ac0abe956468d0d2.png"
   }
]
```
응답코드
- 200 Ok: 조회 성공
- 401 Unauthorized: 인증 실패 (토큰 누락/오류)