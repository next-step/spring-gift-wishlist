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
- '카카오' 포함된 상품명 관련 권한을 `ROLE_MD` 기준으로 변환
  - 해당 권한자가 해당 상품 등록 시, 검증절차 생략
  - 해당 권한자에 한해 validated 값 변환 가능