# 위시 리스트
# (step1) 유효성 검사 및 예외 처리
[O] 의존성 추가  
[O] 요구 사항에 맞는 Validation 기능 구현

# (step2) 회원 로그인 기능 구현
[O] 회원 관리를 위한 Member 테이블 생성
[O] 회원 가입 기능 구현
[O] 로그인 기능 구현

| URL                        | Method | Description              |
|--------------------------|------|--------------------------|
| `/api/members/register`| POST | (회원 가입) 새로운 사용자를 등록한다.   |
| `/api/members/login` | POST | (로그인) 사용자를 인증하고 토큰을 발급한다.|

# (step3) 위시 리스트 기능 구현  
[O] 회원별로 위시 리스트 관리를 위한 Wishlist 테이블 생성
[O] 위시 리스트 상품 조회 기능 구현
[O] 위시 리스트 상품 추가 기능 구현
[O] 위시 리스트 상품 삭제 기능 구현

| URL                        | Method | Description                              |
|----------------------------|-------|------------------------------------------|
| `/api/wishes`              | GET   | (위시리스트 조회) 사용자의 위시리스트를 조회한다.             |
| `/api/wishes`              | POST  | (위시리스트 추가) 현재 사용자의 위시리스트에 상품을 추가한다.      |
| `/api/wishes/{productId}`  | DELETE | (위시리스트 삭제) 현재 사용자의 위시리스트에서 특정 상품을 삭제한다.  |
---
# 상품 관리

# (step1) REST API
| URL                  | Method | Description              |
|----------------------|--------|--------------------------|
| `/api/products`      | GET    | (상품 조회) 전체 상품의 정보를 조회한다. |
| `/api/products/{id}` | GET    | (상품 조회) 특정 상품의 정보를 조회한다. |
| `/api/products`      | POST   | (상품 추가) 새 상품을 추가한다.      |
| `/api/products/{id}` | PUT    | (상품 수정) 특정 상품의 정보를 수정한다. |
| `/api/products/{id}` | DELETE | (상품 삭제) 특정 상품을 삭제한다.     |


# (step2) Admin Page
| URL                  | Method | Description                    |
|----------------------|--------|--------------------------------|
| `/admin`             | GET    | (상품 목록 조회) 전체 상품 목록을 조회한다.     |
| `/admin/add`         | GET    | (상품 등록 페이지) 상품 등록 폼을 조회한다.     |
| `/admin/add`         | POST   | (상품 등록 처리) 새 상품을 등록한다.         |
| `/admin/edit/{id}`   | GET    | (상품 수정 페이지) 특정 상품 수정 폼을 조회한다.  |
| `/admin/edit/{id}`   | POST   | (상품 수정 처리) 특정 상품의 정보를 수정한다.    |
| `/admin/delete/{id}` | POST   | (상품 삭제 처리) 특정 상품을 삭제한다.        |

# (step3) DB Apply
[O] h2 DB 설정하기
[O] 스키마 스크립트, 데이터 스크립트를 각각 schema.sql, data.sql에 작성하기
[O] 상품 정보를 Map 대신 DB에 저장하도록 Repository 클래스 수정하기
