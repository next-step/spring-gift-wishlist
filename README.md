# spring-gift-wishlist

## 1단계: 상품 유효성 검사 및 예외 처리

### 구현 기능 목록

+ [ ] 관련 설정 및 의존성 추가
+ [ ] Testing을 위한 기본 추상 클래스 작성
+ [ ] spring-validation을 이용한 상품 유효성 검사 로직 변경
+ [ ] 예외 처리 로직 구현
+ [ ] 적절한 Logging 추가
+ [ ] 검증을 위한 Test 코드 작성

### 이전 단계 요약

+ [X] 상품 API 구현
+ [X] 관리자 화면 구현
+ [X] 데이터베이스 적용
+ [X] 프로젝트 `spring-gift-wishlist`으로 이관

## 상품 관리 API 명세서

- [상품 조회 API 명세서.md](document/%EC%83%81%ED%92%88%20%EC%A1%B0%ED%9A%8C%20API%20%EB%AA%85%EC%84%B8%EC%84%9C.md)



## 커밋 컨벤션

| type     | meaning      |
|----------|--------------|
| feat     | 새로운 기능 추가    |
| fix      | 오류, 오타 수정    |
| docs     | 문서 생성, 수정    |
| style    | format 변경    |
| refactor | 리팩토링         |
| test     | 테스트 코드 추가/수정 |
| chore    | 유지보수 작업      |

### 커밋 메시지 작성 규칙(AngularJS 컨벤션 기반)

```md
<type>(<scope>): <subject>
// blank line 필수!
<body>
// (footer 입력시)blank line 필수!
<footer>
```
> + 명령형, 소문자 시작, 마침표 없이 작성
> + 필요시 body, footer(이슈번호, breaking change 등) 추가