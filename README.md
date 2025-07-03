# 위시리스트 - 1단계 : 유효성 검사 및 예외 처리

## 미션 1. 상품관리 - 3단계 피드백 반영
    1. (메모리 -> DB) 저장 방식 리팩토링 후 불필요해진 요소 제거

    2. SimpleJdbcInsert 생성자를 통해 초기화 (기존 @PostConstruct 활용)

    3. 네이밍 수정 (기존 private SimpleJdbcInsert insert;)

    4. 컬렉션 팩토리 이용하여 가독성 개선

    5. 매직넘버 (0) 제거


## 미션 2. 위시리스트 - 0단계 피드백 반영
    1. 생성자 접근 제어 및 정적 팩토리 메서드 도입

    2. update 메서드에서 사용되지 않는 반환값 제거

    3. SQL 스크립트 관리를 위한 sql 디렉토리 생성


## 미션 2. 위시리스트 - 1단계 유효성 검사 및 예외 처리
    1. ProductRequest DTO 에 유효성 검사 어노테이션 적용

    2. ProductController 에 유효성 검사 (@Valid) 적용

    3. ProductService 에 '카카오' 포함 유효성 검사 로직 추가 (create 메소드)

    4. GlobalExceptionHandler 와 ExceptionHandler 추가
        -> @RestControllerAdvice 이용

    5. JUnit 기반 Test 코드 작성 
        -> ProductServiceTest - @SpringBootTest 이용
        -> ProductControllerTest - @SpringBootTest + @MockMvc 이용


## 질문
    1. 
    [@Valid를 활용한 DTO 단의 유효성 검사 vs 도메인 객체의 정적 팩토리 메서드에서 유효성 검사]

    @Valid 를 이용하여 DTO 에서 유효성 검사를 진행하면 
    서비스계층으로 전달되기 전에 유효성 검사가 완료되어 
    빠르게 유효하지 않은 데이터가 걸러진다는 장점이 있을 것 같고,
    Product Entity 안에서 정적 팩토리 메소드를 이용하여 유효성 검사를 진행하면
    유효성 검사와 관련된 로직을 모아서 관리하기 때문에 편리할 것 같습니다.
    제 생각에는 Product Entity 의 정적 팩토리 메소드 내에서 유효성 검사를 진행하는게 
    효율적일 것 같은데 리뷰어님은 어떻게 생각하시나요 ?
    -> 과제 수행 과정에서는 두 방법을 혼합했습니다 !













    