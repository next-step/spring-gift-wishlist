# spring-gift-wishlist
# 기본 코드 준비
1. 미션1 상품 관리에서 작성했던 소스코드 로딩

# 유효성 검사 및 예외 처리
1. RequestDto에 글자수 제한, 특수문자 제한 유효성 검사 추가 , Service 레이어에서 제품명에 특정 문구 포함 여부 검사 추가
2. README.md 업데이트
3. 코드 피드백 반영

상품 수정 할 때도 금지 상품명 검사하도록 수정

예외처리 핸들러 주석 업데이트

Service 레이어에 하드 코딩돼있던 금지 단어를 application.properties에 정의, 이에 따른 application.properties 인코딩 방식 UTF-8로 변경

# 회원 로그인
1. Dto, Entity 구현 및 Controller, Service 정의
2. Dto, Entity 수정 및 AuthController 구현
3. ResponseDto 생성자 추가, Entity의 권한 필드 삭제, Service 및 Repository 레이어 구현, users TABLE 구조 선언