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


