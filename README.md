# **spring-gift-wishlist**

***

## 1️⃣ step1: 유효성 검사 및 예외 처리

- [x] 상품명 입력값 형식 검증 (ProductRequestDto)
    - [x] 공백 포함 최대 15자
    - [x] 특수문자 ( ), [ ], +, -, &, /, _ 외 불가
- [x] 검증 실패 시 예외 처리
    - [x] 전역 예외 핸들러 생성
    - [x] 상세 메세지를 포함하는 표준 에러 응답 DTO 반환
- [ ] 상품명에 '카카오'가 포함된 경우 처리
    - [ ] 상품 정보에 status 필드 추가
    - [ ] service 계층에서 상품 생성시 '카카오' 포함 여부 체크
        - [ ] 승인 필요 단어 체크 로직 분리 생성
        - [ ] 포함 시 status를 PENDING_APPROVAL로 설정 및 저장
        - [ ] 미포함 시 status를 APPROVED로 설정 및 저장
    - [ ] 관리자(MD) 기능 구현
        - [ ] AdminController로 관리자만 접근할 수 있는 상품 상태 변경 기능