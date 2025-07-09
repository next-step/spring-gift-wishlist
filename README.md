# spring-gift-wishlist

## STEP-00 상품관리 코드 이전
- 디렉터리 직접 이동하여 파일 수정

## STEP-01 유효성 검사

### 요구사항 구현 방식
- spring-boot-starter-validation을 사용하여 유효성 검사
- NotBlank 및 NotNull을 사용하여 이름과 가격에 빈 값을 보낼 수 없도록 함
- 이름에 Size 어노테이션 사용: 최대 사이즈 15로 설정
- 이름에 Pattern을 사용하여 특수 기호 처리 (정규식 표현 사용)
- 가격에 Min 어노테이션 사용: 최소 가격은 100원으로 설정
- '카카오' 이름이 들어갔을 때 처리
  - 이름을 등록할 때 MD와 협의했는지 체크박스 처리
  - 체크하면 등록 됨
  - 체크하지 않았다면 등록 페이지 리턴(수정 필요)

### 수정 사항 및 개선 사항 
- *문제 상황 1.*: 
  - 카카오 이름이 들어갔을 때 단순히 사용자가 직접 체크하는 방식으로 등록의 여부를 시스템에서 판단
- *해결 방법*: 
  - 카카오 이름이 들어간 상품이 있을 때에 시스템에서 보류사항으로 처리 
  - 관리자가 보류사항 페이지에서 해당 상품을 처리한 후 리스트에 체크

- ~~문제 상황 2.~~: (✅*해결완료*)
  - 유효성 검사 후 통과하지 못하면 단순히 입력 칸 옆에 오류 상황 표시
- *해결 방법*:
  - 알림 창으로 표시하면 좀 더 사용자 입장에서 편하지 않을까?라는 생각

- ~~문제 상황 3.~~: (✅*해결완료*)
  - @AssertTrue에 대한 메세지('카카오'가 포함된 상품명은 담당 MD와 협의가 필요합니다.)가 사용자 입장에서 보이지 않음
  - 전역 오류로 표시하면 해결 가능한 줄 알았는데 해결되지 않았음
- *해결 방법*:
  - 알아봐야합니다...!
  - STEP2에서는 해결할 수 있도록!

## STEP-02 회원 로그인
### *(현재 단순 로그인 API 구현 완료 - 물품에 대한 파일 분리는 정리 필요)* 
### 요구사항 구현 방식
1. 회원가입
  - api/members/register
  - POST
  - 사용자 이메일과 비밀번호를 전달
  - 토큰 응답
2. 로그인
  - api/members/login
  - POST
  - 사용자 이메일과 비밀번호 전달
  - 토큰 응답

### 폴더 구조 변경 사항
- controller / model / dto / repository 기능 별로 폴더 분리
---
- controller
  - ProductController
  - UserController
- model
  - user
  - product
- dto
  - product
    - ProductRequestDto
    - ProductResponseDto
  - user
    - UserRequestDto
    - UserResponseDto
- repository
  - ProductRepository
  - UserRepository