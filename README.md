# spring-gift-wishlist

## 1단계: 상품 유효성 검사 및 예외 처리

### 구현 기능 목록

+ [X] 관련 설정 및 의존성 추가
+ [X] Testing을 위한 기본 추상 클래스 작성
+ [X] spring-validation을 이용한 상품 유효성 검사 로직 변경
  + [X] Custom Validator 구현
  + [X] ProductController 에서 @Valid 어노테이션을 이용한 유효성 검사 적용
+ [X] 예외 처리 로직 구현
+ [X] 적절한 Logging 추가
+ [X] 검증을 위한 Test 코드 작성

## 2단계: 회원 로그인 구현

### 구현 기능 목록

+ [X] SQL 데이터베이스 적용 (스키마 수정 및 설계)
+ [X] 상품관리 요청에 user 정보 추가 수정
+ [X] 회원 엔티티 및 레포지토리 구현
+ [X] 회원 서비스 + 컨트롤러 구현
+ [X] JWT 유틸리티 클래스 작성
+ [X] JWT 및 인터셉터 기반 인증 / 인가 구현
+ [X] Thymeleaf 기반 UI 및 레이아웃 구조 개션
+ [X] 로그인 페이지 구현
+ [X] 회원가입 페이지 구현
+ [X] 로그인 / 회원가입 기능 구현

# 3단계 : 위시리스트 

### 구현 기능 목록 

+ [X] 위시리스트를 스키마 설계 및 추가
+ [X] 위시리스트 엔티티 구현
+ [X] 위시리스트 dao 구현
+ [X] 위시리스트 repository 구현
+ [X] 위시리스트 서비스 구현
+ [X] 위시리스트 컨트롤러 구현
+ [X] 위시리스트 테스트 코드 작성

### 기능별 API 요약
+ 다건 위시리스트 조회 : `GET /api/wishlist`
+ 단건 위시리스트 조회 : `GET /api/wishlist/{id}`
+ 위시리스트 생성 : `POST /api/wishlist`
+ 위시리스트 수정 : `PUT /api/wishlist/{id}`
  + 상품 수량만 수정 가능(0일 시 삭제)
+ 위시리스트 삭제 : `DELETE /api/wishlist/{id}`

### 이전 단계 요약

+ [X] 상품 API 구현
+ [X] 관리자 화면 구현
+ [X] 데이터베이스 적용
+ [X] 프로젝트 `spring-gift-wishlist`으로 이관

## API 명세서

- [상품 조회 API 명세서.md](document/%EC%83%81%ED%92%88%20%EC%A1%B0%ED%9A%8C%20API%20%EB%AA%85%EC%84%B8%EC%84%9C.md)
- [사용자 API 명세서.md](document/%EC%82%AC%EC%9A%A9%EC%9E%90%20API%20%EB%AA%85%EC%84%B8%EC%84%9C.md)
- [인증 API 명세서.md](document/%EC%9D%B8%EC%A6%9D%20API%20%EB%AA%85%EC%84%B8%EC%84%9C.md)


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