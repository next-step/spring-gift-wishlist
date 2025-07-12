# step 0

- 상품 관리 기능 가져오기

# step1 구현 기능

- 상품 이름에 들어올 수 있는 입력 체크

    1. 최대 15글자(공백 포함)

    2. (),[],+,-,&,/,_ 에 해당하는 특수문자만 사용가능

    3. "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용

# step2 구현 기능

- 회원가입 기능 구현

    - POST /api/members/register

    - 회원가입 성공시 CREATE 상태코드, Token 반환

    - 똑같은 이메일로 회원가입 시도 시 BAD REQUEST 반환

- 회원로그인 기능 구현

    - POST /api/members/login

    - 로그인 성공시 OK 상태코드, Token 반환

    - 회원가입하지 않은 이메일로 로그인 시 UNAUTHORIZED 반환

    - 잘못된 비밀번호로 로그인 시 UNAUTHORIZED 반환

- 회원을 조회, 추가, 수정 삭제할 수 있는 관리자 화면 구현

    - /admin/members : 회원 목록

    - /admin/members/new : 회원 등록

    - /admin/members/{id}/update : 회원 수정

    - /admin/members/{id}/delete: 회원 삭제

# step3 구현 기능

- HandlerMethodArgumentResolver 구현
    - 위를 통해서 토큰을 Member 객체로 변환해 매개변수에 넣어줌.
- 위시 리스트 등록, 조회, 수정, 삭제 기능 구현
    - 등록: POST /api/wishes
        - 매개변수: Member, WishRequestDto
    - 조회: GET /api/wishes
        - 매개변수: Member
    - 수정(수량 수정): PATCH /api/wishes
        - 매개변수: Member, WishRequestDto
    - 삭제: DELETE /api/wishes
        - 매개변수: Member
