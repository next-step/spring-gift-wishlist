# 위시리스트 - 1단계

## 미션 1. 상품관리 - 3단계 피드백 반영
    1. (메모리 -> DB) 저장 방식 리팩토링 후 불필요해진 요소 제거

    2. SimpleJdbcInsert 생성자를 통해 초기화 (기존 @PostConstruct 활용)

    3. 네이밍 수정 (기존 private SimpleJdbcInsert insert;)

    4. 컬렉션 팩토리 이용하여 가독성 개선

    5. 매직넘버 (0) 제거


## 미션 2. 위시리스트

### WishlistController
    [POST] /api/wishes  위시리스트 상품 추가


    [DELETE] /api/wishes/{wishId} 위시리스트 상품 삭제


    [GET] /api/wishes?page=0&size=10&sort=createdDate,desc 위시리스트 상품 조회

    
### Wish Entity

### WishService

### WishRepository

    