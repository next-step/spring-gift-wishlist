# 미션 2. 위시리스트 - 2단계 회원 로그인

## Member 도메인 클래스 
    정적 팩토리 메소드 사용하여 객체 생성 전에 유효성 검사를 진행
    -> validateEmail(), validatePassword()

    유효성 검사 로직은 Member 도메인 클래스 내에 존재
    -> 도메인 레벨에서 검증하여 어떤 경로로 객체가 생성되든 로직 적용

    정적 팩토리 메소드를 of() 와 create() 로 분리 
    -> of() 는 DB 조회한 데이터로 객체 생성
    -> create() 는 새로운 회원가입을 위한 객체 생성

## DTO
    MemberRequest 와 AuthResponse 
    Bean Validation 이용하여
    도메인 검증 + DTO 검증 (이중 검증)

## 비밀번호 암호화 진행
    PasswordConfig 생성 
    BCryptPasswordEncoder 사용

## MemberRepository 
    SimpleJdbcInsert 이용하여 ID 자동 생성
    회원저장 save() 메소드
    이메일로 회원 찾기 findByEmail() 메소드
    ID로 회원 찾기 findById() 메소드 

## MemberService
    회원가입 register() 메소드 
    -> 비밀번호 암호화 진행
    -> 이메일 중복 체크
    -> Member 객체 생성 및 저장
    -> 토큰 생성 및 반환

    로그인 login() 메소드
    -> 이메일로 회원 찾기
    -> 비밀번호 검증
    -> 토큰 생성 및 반환

    ID로 회원 찾기 findById() 메소드 


## JWT 
    JWT 토큰 활용을 위한 의존성 추가 및 설정 추가
    
    JwtTokenProvider 클래스 생성 
    -> 토큰 생성 createToken() 메소드
    -> 토큰에서 회원 ID 추출 getMemberId() 메소드
    -> 토큰 유효성 검증 validateToken() 메소드 
    

## MemberController 
    회원가입 [POST] /api/members/register
    로그인 [POST] /api/members/login
    -> 로그인은 토큰을 생성하기도 하고 
    -> GET 요청은 파라미터에 비밀번호 노출 위험이 있기 때문에
    -> POST 요청 사용