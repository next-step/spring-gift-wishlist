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
### 코드리뷰 코멘트 반영
- Dto 명칭 변경 및 분리
- Product의 Setter 삭제, update메소드(신규 객체 반환)를 통한 대체
- Service 레이어의 2종 검사를 Product로의 역할 재할당 및 신규 객체 반환처리
  - 각 파라미터 null 여부 검사
  - 상품명의 '카카오' 등 금지단어 포함여부 검사
- GlobalExceptionHandler의 404/500 에러 Handling
- SpringBootTest 실행 순서가 결과에 영향을 주지 않도록 조치
- SpringBootTest 검사 중 response body의 값을 notnull이 아닌 실제 값 검사를 수행하도록 조치

## 2단계 코드리뷰 요청 (구현내용 명세)
### 토큰기법을 제외한 기능구현
- `POST /api/members`를 통한 유저 등록
  - email, password 필요. email과 password 각 유효성검사 수행
- `POST /api/members/login`를 통한 로그인 수행
  - email, password 필요. email & password 쌍에 대해 정보가 존재하는지 확인
### 토큰기법을 추가한 기능 구현
- 위 기능의 반환값으로 토큰 반환받도록 구현, admin page 접근제한 추가
- 테스트코드 작성
### 코드리뷰 전 추가구현
- 유저등록 URL 변경 (`/api/members` -> `/api/members/register`)
- API문서 정리
- 권한 다양화 (현재 `ROLE_USER`, `ROLE_ADMIN` 2종 : `ROLE_SELLER`, `ROLE_MD`, `ROLE_CS` 추가)
  - 비권한자
    - 상품 열람 가능
    - 회원가입/로그인 가능
  - `ROLE_USER`
    - 회원가입 시 기본 제공 권한
    - 추후 위시리스트 기능에서 위시리스트 관련 권한 추가
  - `ROLE_SELLER`
    - 상품 등록 권한
    - 추후 본인이 등록한 상품에 한해 수정/삭제 기능 추가
  - `ROLE_ADMIN`
    - 사용자 열람, 등록, 수정, 삭제, 권한변경 권한
    - 해당 AdminPage 접근 가능
  - `ROLE_MD`
    - 상품 등록, 수정, 삭제, 검증 권한
    - 해당 AdminPage 접근 가능
    - 추후 본인이 등록한 상품만 수정/삭제 가능하도록 제한
  - `ROLE_CS`
    - 사용자 열람 권한
    - 해당 AdminPage 접근 가능
### 코드리뷰 반영
- Spring Security 적용 제외
### 추가 구현
- Admin Page 로그인 기능 구현
- 테스트 코드 리팩터링

## 3단계 코드리뷰 요청 (구현내용 명세)
### 2단계 코드리뷰 반영 및 추가 구현
- Member AdminPage 기능 구현
- Admin Page 디자인 개선
- AuthorizationFilter 가독성 향상
- 토큰 추출 수행위치 변경 (`JwtTokenProvider` -> `JwtTokenFilter`)
- BCryptEncryptor의 불필요한 `@Component` 제거
### 위시리스트 기능 구현
- product 테이블의 soft delete 기능 구현
  - 구현목적 : 상품이 삭제되더라도, 단순히 구매가 불가하게 할 뿐 상품 정보는 남겨두기 위함
  - 활용내용 : 위시리스트에 담아둔 상품이 삭제되더라도, 그 상품에 대한 정보는 사용자가 볼 수 있게끔 함
    - 사용자가 위시리스트에 담아두는 목적이, 꼭 그 상품id에 해당하는 상품은 아니라는 생각 하
    - 해당 상품을 판매하고있는 다른 판매자에게라도 그 상품을 구매하게끔 안내할 수 있을 것
  - 구현내용
    - product 테이블에 deleted 컬럼(bit) 추가
- 위시리스트 기능 구현 (조회/추가/삭제)
  - 테이블 구성
    - id : 위시 아이템 ID (PK)
    - product_id : 위시리스트에 담긴 상품 ID (FK, product.id)
    - member_id : 위시리스트를 등록한 회원 ID (FK, member.identify_number)
    - created_at : 위시리스트 등록일시
    - 이때, (product_id, member_id) 조합은 유니크하도록 설정
  - `GET /api/wishes`를 통한 위시리스트 조회
    - 위시리스트에 담긴 상품ID를 통해 상품정보를 조회하여 반환
    - 상품이 삭제된 경우에도 상품정보는 반환되도록 하나, 각 상품에 대해 구매가능여부를 추가로 명시되도록 함 (상품이 삭제된 경우 구매불가)
  - `POST /api/wishes`를 통한 위시리스트 추가
    - 위시리스트에 담고자 하는 상품ID를 통해 상품정보를 조회하여, 해당 상품이 존재하는 경우에만 위시리스트에 추가
  - `DELETE /api/wishes/{id}`를 통한 위시리스트 삭제
    - 위시리스트의 WishItem ID를 통해 위시리스트 아이템 삭제
    - 상품의 ID가 아님에 유의
- 테스트코드 작성
  - 위시리스트 기능 포함
