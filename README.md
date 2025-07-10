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
- [x] 관리자 화면 HTML 파일에 카카오를 포함하는 상품명이 MD 협의가 됐는지에 관한 체크박스 추가
- [x] 테스트 코드 작성(성공 시나리오)
- [x] MD와 협의 여부를 데이터베이스에 저장시키기 위해  `schema.sql` 수정 
- [x] 협의 여부 속성 추가에 따른 전체 코드 리팩토링 && view를 위한 dto 구현하여 ProductRequestDto 부담 줄이기

# 2단계 - 회원 로그인

## 기능 목록

- [x] member 관련 파일을 담을 패키지, product 관련 파일을 담을 패키지 생성
- [x] member 엔티티 구현 및 데이터베이스 구현
- [ ] member 관련 API 구현
  - [x] member 테이블에 접근하여 생성,조회,수정,삭제를 할 수 있는 MemberRepository 구현
  - [x] CRUD에 대한 비즈니스 로직을 수행하는 MemberService 구현
  - [x] 회원 생성, 조회, 수정, 삭제 API 기능을 수행하는 MemberController 구현(관리자용)
  - [x] (선택) 회원을 조회, 추가, 수정, 삭제할 수 있는 관리자 화면을 구현
- [x] 회원 인증/인가 기능 추가 
  - [x] JJWT 라이브러리 사용을 위한 의존성 추가
  - [x] 로그인 요청을 보낼 때 사용하는 LoginRequestDto 생성
  - [x] 회원 인증 JWT를 발급하는 TokenProvider 구현
  - [x] 발급된 토큰을 Controller에 전달하는 AuthService 구현
  - [x] TokenResponseDto 구현 및 dto 계층 세분화
  - [x] 회원가입, 로그인 API 기능을 수행하는 MemberAuthController 구현
-[x] 관리자 전용 API에 관리자만 접근할 수 있도록 구현
  - [x] TokenProvider에 토큰이 유효한지 검사하는 메서드 구현
  - [x] 토큰을 복호화하여 claims에서 role의 값을 반환하는 메서드 구현
  - [x] 관리자 권한 검사용 AdminInterceptor 구현
  - [x] feat: WebConfig 파일을 만들어 AdminInterceptor에 /admin/** 경로 등록
- [x] 관리자 등록을 위해 data.sql에 INSERT 문 생성
- [x] 예외 처리 및 유효성 검사
  - [x] Member의 요청 DTO에 Validation 어노테이션을 사용해 유효성 검사
  - [x] Controller에 @Valid 추가
  - [x] 상황에 맞는 예외 클래스 생성 및 모든 예외 GlobalExceptionHandler에서 처리하도록 구현
- [x] 비밀번호를 암호화하여 저장하기 위해 PasswordUtil 추가(salt, SHA-256 활용)
- [x] MemberAdminController && MemberAuthController에 대한 테스트 추가


### 관리자 전용 회원 관리 API

| 기능            | Method | 경로                          | 요청 Body                                                     | 응답 Status       | 응답 Body                                                     |
|-----------------|--------|-------------------------------|---------------------------------------------------------------|-------------------|---------------------------------------------------------------|
| 회원 추가       | POST   | `/admin/members`              | `{"email":"...","password":"...","role":"USER"}`              | `201 Created`     | `{"id":1,"email":"...","role":"USER"}`                        |
| 전체 회원 조회  | GET    | `/admin/members`              | —                                                             | `200 OK`          | `[{"id":1,"email":"...","role":"USER"}, ...]`                  |
| 단일 회원 조회  | GET    | `/admin/members/{id}`         | —                                                             | `200 OK`          | `{"id":1,"email":"...","role":"USER"}`                        | 
| 회원 정보 수정  | PUT    | `/admin/members/{id}`         | `{"email":"...","password":"...","role":"ADMIN"}`             | `200 OK`          | `{"id":1,"email":"...","role":"ADMIN"}`                       |  
| 회원 삭제       | DELETE | `/admin/members/{id}`         | —                                                             | `204 No Content`  | —                                                             |  


### 회원 로그인 API
| 기능   | Method | 경로                          | 요청 Body                                        | 응답 Status   | 응답 Body                                        | 
|------|--------|-------------------------------|--------------------------------------------------|---------------|--------------------------------------------------|
| 회원가입 | POST   | `/api/members/register`       | `{"email": "...", "password": "..."}`            | `201 Created` | `{"token":"<JWT 토큰 문자열>"}`                  | 
| 로그인  | POST   | `/api/members/login`          | `{"email": "...", "password": "..."}`            | `200 OK`      | `{"token":"<JWT 토큰 문자열>"}`                  |

  
# 3단계 - 위시 리스트

## 기능 목록

- [x] `schema.sql`에 위시리스트 테이블 CREATE문 추가