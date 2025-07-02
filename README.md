# spring-gift-wishlist

## 0단계 - 기본 코드 준비

### 구현할 기능 목록

1. 상품 관리 레포지토리를 remote add, fetch 한다.
2. cherry-pick을 통해 상품 관리의 1,2,3단계 커밋들을 가져온다.
3. 해당 커밋들을 squash 한다.

## 1단계 - 유효성 검사

### 구현할 기능 목록

1. 기존 ProductCreateRequestDto, ProductUpdateRequestDto에 지정해둔 validation 어노테이션들을 과제 조건에 맞게 수정한다.
2. 상품명에 카카오가 포함된 경우는 Service 단에서 검증하여 exception을 던진다. (추후 회원 기능이 추가되면 MD 협의 여부를 체크하는 로직 작성하여 통과 여부 결정)
