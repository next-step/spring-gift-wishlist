# 미션 2. 위시리스트 - 3단계 위시리스트

1. 회원가입, 로그인을 통해 생성된 토큰을 사용하여 사용자별 위시리스트를 구현
    1. 위시리스트에 등록된 상품 목록을 조회할 수 있도록 (상품 목록 조회)
    2. 위시리스트에 상품을 추가할 수 있도록 (상품 추가)
    3. 위시리스트에 담긴 상품을 삭제할 수 있도록 (상품 삭제)

---

## Wish 도메인 클래스와 wish 테이블

    (편의상 위시 = 찜으로 표현)

    id : 각 찜 항목의 고유 식별자 (wishId), 삭제 시 사용
    memberId : 해당 찜 항목을 만든 사용자 ID
    productId : 찜한 상품의 ID
    createdAt : 위시리스트에 해당 상품을 추가한 시점, 정렬 기준에 활용

---

## WishRepository 와 JdbcWishRepositoryImpl

    WishRepository 인터페이스를 이용하여 추상화
    JdbcWishRepositoryImpl 구현체를 활용
    (기존에는 구현 클래스만 만들었는데 이번에는 인터페이스와 구현체로 분리해보았습니다.)
    
    addWish() : 위시리스트에 상품 추가
    removeWishById() : 위시리스트에서 상품 삭제
    getWishlistByMemberId() : 사용자별 위시리스트 조회
    isWished() : 중복 찜 확인
    findById() : wishId 로 Wish 조회

---

## WishService
    사용자별 위시리스트 서비스 로직 구현
    addWish() : 중복 체크 후 찜 항목 추가, 이미 찜한 경우 예외 발생
    removeWish(): 자신의 위시리스트 항목한 삭제 가능, 멱등성 보장
    getWishlist(): 사용자별 찜 목록 조회, 없으면 빈 리스트 반환


---

## WishRequest DTO 와 WishResponse DTO
    WishRequest record 와 WishResponse recored
    WishRequest DTO 는 productId 만 가지고 있음.
    WishResponse DTO 는 사용자 ID 를 제외한 위시 상품 정보

---

## WishController
    인증된 사용자의 위시리스트에 상품 추가, 삭제, 조회 기능 제공
    @LoginMember를 활용하여 인증된 Member 객체 주입
    [POST] /api/wishes
    [DELETE] /api/wishes/{wishId} 
    [GET] /api/wishes

---

## @LoginMember와 LoginMemberArgumentResolver, WebConfig
    @LoginMember 애노테이션 정의
    JwtAuthenticationFilter 에서 request.setAttribute("memberId", memberId) 저장
    LoginMemberArgumentResolver 를 통해 MemberService에서 회원 조회 후 컨트롤러에 자동 주입
    WebMvcConfigurer에 해당 리졸버 등록하여 전역 적용
    

---

## E2E 통합 테스트
    시나리오
    1. 회원가입 → 로그인 → 토큰 발급
    이메일, 비밀번호로 회원가입
    로그인하여 JWT 토큰 획득

    2. 상품 등록
    위시리스트에 담을 상품을 하나 생성
    
    3. 찜 추가
    JWT 토큰을 헤더에 담아 POST /api/wishes 호출
    정상적으로 위시리스트에 상품이 추가되는지 확인
    
    4. 찜 중복 방지
    같은 상품을 다시 찜하면 예외 발생 여부 확인 (이미 찜한 상품입니다.)
    
    5. 찜 목록 조회
    GET /api/wishes 호출 시 해당 사용자의 찜 리스트가 잘 나오는지 확인
    
    6. 찜 삭제
    DELETE /api/wishes/{wishId} 호출 시 정상적으로 삭제되는지 확인
    존재하지 않는 항목이나 타인의 항목 삭제 시 예외 발생 여부 확인






