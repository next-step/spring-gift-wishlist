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

### 2단계 구현사항
-[x] [개발 전 환경 세팅] 위시리스트 - 2단계 수행을 위한 build.gradle, application.properties 수정
-[x] [Domain] 회원 도메인 설계 (Member Class) 및 역할에 대한 설계(Role Enum Class)
-[x] [SQL] 회원에 대한 table 정의하는 sql 추가 (schema.sql)
-[x] [Dto] 로그인 요청 및 응답에 대한 DTO 설계 (MemberLoginRequestDto, MemberLoginResponseDto)
-[x] [Dto] 회원의 정보를 반환하는 DTO 설계 (MemberInfoResponseDto)
-[x] [Repository] 회원 Domain에 대한 DB CRUD 수행하는 인터페이스 및 구현체 작성 (MemberRepository, JdbcTemplateMemberRepository)
-[x] [Service] 회원에 대한 가입, 로그인 및 관리자 화면에 사용되는 기능을 포함한 인터페이스 및 구현체 작성 (MemberService, MemberServiceImpl)
-[x] [Security] JWT 이용한 Token 생성 클래스 설계 (JwtProvider)
-[x] [Security] 원활한 테스트를 위한 CSRF disable 위한 Config 클래스 설계(SecurityConfig)
-[x] [Controller] 회원 가입, 로그인을 요청 처리하는 컨트롤러 설계(MemberController)
-[x] [Controller] 회원 조회, 추가, 수정, 삭제할 수 있는 관리자 화면 요청 처리하는 컨트롤러 설계 (MemberAdminController)
-[x] [Templates] 회원 조회, 추가, 수정, 삭제를 위한 thymeleaf 기반 관리자 페이지 3개 (member-create-form, member-list, member-update)
-[x] [Exception] 이메일이 이미 사용되는 경우, 해당 이메일이 존재하지 않는 경우, 비밀번호 불일치의 경우에 대한 커스텀 예외 작성 (EmailAlreadyRegisteredException, MemberNotFoundException, PasswordMismatchException)
-[x] [Exception] 그에 대한 GlobalExceptionHandler 수정
-[x] [Test] 테스트 코드 도메인 별 디렉토리 분리

### 3단계 구현사항
-[x] [SQL] 위시 리스트에 대한 table 정의하는 sql 추가 (schema.sql)
-[x] [Domain] 위시 리스트 도메인 설계 (WishList Class)
-[x] [Dto] 위시 리스트 추가, 수정, 결과 반환에 대한 DTO 설계 (차례대로 CreateRequestDto, UpdateRequestDto, ResponseDto 순)
-[x] [Repository] 위시 리스트 도메인에 대한 DB CRUD 수행하는 인터페이스 및 구현체 작성 (WishListRepository, JdbcTemplateWishListRepository)
-[x] [Service] 위시 리스트 추가, 전체 조회, 수정, 삭제, 전체 삭제의 역할 담당하는 인터페이스 및 구현체 작성 (WishListService, WishListServiceImpl)
-[x] [Security] 지정된 경로에 대해서 인증 작업 수행하고 memberId를 얻어 Controller에게 전달하는 Filter 구현 (JwtAuthenticationFilter)
-[x] [Security] JwtAuthenticationFilter 에 대해 필터 순서 등록하는 FilterConfig 정의
-[x] [Controller] 위시 리스트 추가, 전체 조회, 수정, 삭제, 전체 삭제의 요청 처리하는 컨트롤러 설계 (WishListController)
-[x] [Exception] 상품이 존재하지 않는 경우에 대한 커스텀 예외 ProductNotFoundException 정의 (기존 NoSuchElementException 삭제)
-[x] [Exception] 위시 리스트에 사용자 id와 상품 id에 대응되는 항목이 없을 때 발생하는 WishListItemNotFoundException 정의
-[x] [Exception] 위 2가지 커스텀 Exception에 대해서 GlobalExcpetionHandler 수정
-[x] [Refactor : Service] NoSuchElementException -> ProductNotFoundException 변경에 따른 리팩토링
-[x] [Test] 위시 리스트 도메인에 대한 테스트 코드 작성 (WishListTest)
