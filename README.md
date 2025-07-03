# spring-gift-wishlist

## 구현할 기능 목록 (0단계 - 코드복사)

- [x] 이전 과정 코드 복사

## 구현할 기능 목록 (1단계 - 유효성 검사 및 예외 처리)

- [x] `build.gradle`에 `validation` 의존성 추가
- [x] `ProductRequest` DTO에 상품명 유효성 검사 추가
    - [x] 이름 길이 15자 이하 제한
    - [x] 허용된 특수문자 외 사용 금지
    - [x] "카카오" 포함 금지 (Custom Validator)
- [x] Controller에 `@Valid` 애노테이션 적용
- [x] REST API를 위한 글로벌 예외 처리기 (`@RestControllerAdvice`) 구현
- [x] 관리자 페이지 폼 유효성 검사 실패 처리
