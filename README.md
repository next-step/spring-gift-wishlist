# spring-gift-wishlist
## step0 기본 코드 준비
- [x] spring-gift-product 코드 옮기기
## step1 유효성 검사 및 예외처리
- [x] 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
- [x] 가능: ( ), [ ], +, -, &, /, _ 그 외 특수 문자 사용 불가
- [x] "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.(어미에 카테캠을 사용하면 가능하게(어미에 카테캠을 사용하면 된다는 정보=MD와 협의되었음을 의미))
- [x] 가격은 0 이상이어야 한다
## step2 회원로그인
- [x] 도메인별로 패키지 리팩토링
- [x] AuthInterceptor로 토큰검증, 검증범위 : /api/**, 제외범위 : /api/members/register, /api/members/login, /admin/products
- [x] 회원을 조회, 추가, 수정, 삭제할 수 있는 관리자 화면을 구현