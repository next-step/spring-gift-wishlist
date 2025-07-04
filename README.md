# spring-gift-wishlist

### 이전 상품관리 - 최종 피드백 반영사항
-[x] 존재하지 않는 상품에 대해서 "단건조회, 수정, 삭제" 요청시 예외처리 Service Layer 전담, 이에따른 Repository Layer 예외처리 로직 삭제(ProductServiceImpl, JdbcTemplateProductRepository)

### 1단계 구현사항

-[x] build.gradle 의존사항 validation 추가
-[x] ProductRequestDto 필드 BeanValidation 검사 추가 (15자초과, 허용되지 않는 문자, 가격 0원이상, not null)
-[x] ProductRequestDto 변화 기반, Controller 리팩토링 (ProductController, ProductAdminController)
-[x] "카카오" 포함시 Validation 수행하는 ProductNameValidator 개발
-[x] ProductServiceImpl에 ProductNameValidator 검증 추가 (ProductServiceImpl)
-[x] "카카오" 관련 검증 실패시 커스텀 InvalidRequestException 구현
-[x] @RestControllerAdvice, @ExceptionHandler 조합한 GlobalExceptionHandler 개발
-[x] 테스트 개발(검증 로직 정상 동작 유무 확인)
-[x] 상품 추가, 수정 화면에서 카카오 관련 md 여부 확인 체크 박스 추가 

### 1단계 피드백 반영 사항
-[x] 사용자 API에서 MD 승인 여부 조작 방지, 관지라 API에 대해서만 접근 가능토록 변경
