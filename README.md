# spring-gift-product
## step1 상품 API("/api/products")
* 전체 조회 
* 개별 조회 ("/{id}")
* 상품 추가
* 상품 수정 ("/{id}")
* 상품 삭제 ("/{id}")
## step2 관리자 화면
* 상품 목록 조회 화면
* 상품 추가/수정 화면
* 상품 삭제
## step3 데이터베이스 연동
* product repository를 jdbctemplate 활용방식으로 변경함
---
# spring-gift-wishlist
## step0 기본 코드 준비
- [x] spring-gift-product 코드 옮기기
## step1 유효성 검사 및 예외처리
- [x] 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
- [x] 가능: ( ), [ ], +, -, &, /, _ 그 외 특수 문자 사용 불가
- [x] "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.