# spring-gift-wishlist

## 🎁 주요 구현 내용

#### 1. 예외 처리 시스템 구축
- **역할 분리**: 사용자에게 HTML 페이지를 반환하는 관리자(MVC)와 JSON 데이터를 응답하는 API(REST)의 예외 처리를 `MvcExceptionHandler`와 `ApiExceptionHandler`로 분리했다.
- **중앙 관리**: `@ControllerAdvice`를 사용하여 각 컨트롤러 패키지에서 발생하는 예외를 한곳에서 중앙 관리하도록 설계했다.
- **상세 예외 처리**: `ProductNotFoundException`(404), `MethodArgumentTypeMismatchException`(400), `HttpMessageNotReadableException`(400) 등 다양한 예외 상황에 맞는 HTTP 상태 코드와 명확한 에러 메시지를 반환하도록 구현했다.

#### 2. 서버 측 유효성 검사 강화
- **커스텀 Validator**: 상품명에 대한 비즈니스 규칙(글자 수, 특수문자, 금지어)을 검증하기 위해 `@ValidProductName` 커스텀 어노테이션과 `ProductNameValidator`를 구현했다.
- **DTO 적용**: 상품 추가/수정 DTO에 `@Valid`를 적용하여 컨트롤러 단에서 데이터의 유효성을 우선 검증하도록 했다.

#### 3. 코드 리팩토링 및 안정성 확보
- **서비스 및 레포지토리**: 실패 시 `Optional`이나 `boolean`을 반환하는 대신, 명시적인 예외를 발생시키도록 로직을 개선하여 역할과 책임을 명확히 했다.
- **트랜잭션 관리**: 데이터의 상태를 변경하는 모든 서비스 메서드에 `@Transactional`을 적용하여 데이터의 일관성과 무결성을 보장했다.

#### 4. 테스트 코드 구현
- 애플리케이션의 안정성 확보를 위해 주요 로직에 대한 단위 및 통합 테스트를 작성했다.
- **Validator 단위 테스트**: 순수 로직의 모든 경우의 수를 검증했다.
- **Service 통합 테스트**: DB 연동을 포함한 핵심 비즈니스 흐름(CRUD)을 검증했다.
- **Controller 통합 테스트**: `MockMvc`를 사용하여 HTTP 요청/응답 및 예외 처리 동작을 검증했다.


## 📂 프로젝트 구조

주요 패키지 구조는 역할과 책임에 따라 다음과 같이 분리했다.


└── src
├── main
│   ├── java
│   │   └── gift
│   │       ├── controller
│   │       │   ├── admin  // 관리자용 MVC 컨트롤러
│   │       │   └── api    // 외부용 REST API 컨트롤러
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception  // 예외 및 전역 예외 처리기
│   │       ├── repository
│   │       ├── service
│   │       └── validation // 커스텀 유효성 검사기
│   └── resources
│       ├── templates
│       │   ├── admin/products
│       │   └── error      // 상태 코드별 에러 페이지
│       └── data.sql
└── test
└── java
└── gift           // 주요 계층별 테스트 코드 (controller, service, validation)