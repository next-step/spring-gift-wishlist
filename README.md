# spring-gift-wishlist

## 3단계 - 위시 리스트 구현

### step2 코드 리팩토링

### step3 기능 구현
- [ ] 데이터베이스 스키마 변경: 위시 리스트 정보를 저장할 wish 테이블을 schema.sql에 추가한다.
- [ ] 위시 리스트 도메인 구현: Wish 엔티티, DTO, Repository, Service를 구현한다.
- [ ] ArgumentResolver 구현: @LoginMember 어노테이션과 LoginMemberArgumentResolver를 구현하여, 컨트롤러가 인증된 사용자 정보를 쉽게 주입받도록 한다.
- [ ] WebConfig 설정: 구현한 ArgumentResolver를 Spring MVC에 등록한다.
- [ ] 위시 리스트 API 구현:
  - [ ] GET /api/wishes: 현재 로그인한 사용자의 위시 리스트 목록을 조회한다.
  - [ ] POST /api/wishes: 위시 리스트에 상품을 추가한다.
  - [ ] DELETE /api/wishes/{productId}: 위시 리스트에서 특정 상품을 삭제한다.
- [ ] 예외 처리: 이미 위시 리스트에 있는 상품을 추가하려는 경우 등 예외를 처리한다.