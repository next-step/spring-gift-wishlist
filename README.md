# 위시리스트 미션
# 0단계 - 기본 코드 준비

## 기능 목록

- [x] 이전 미션(상품 관리)의 코드를 위시리스트 미션으로 옮기기
    - [x] 의존성 옮기기
    - [x] 리소스 파일, 프로퍼티 파일, 테스트 코드 모두 이동

# 1단계 - 유효성 검사 및 예외 처리

## 기능 목록

- [x] 유효성 검사를 위한 Validation 의존성 추가
- [x] 상품의 이름 유효성 검사
  - [x] 공백 포함 최대 15자 길이 제한
  - [x] 허용된 특수문자 외 사용 불가
  - [x] "카카오"가 포함된 상품 이름은 MD 허용이 없을 경우 불가
- [x] GlobalExceptionHandler 구현
  - [x] `MethodArgumentNotValidException` 처리
  - [x] 존재하지 않는 id에 대한 예외(`ProductNotFoundException`) 추가 및 예외 처리 기능 추가