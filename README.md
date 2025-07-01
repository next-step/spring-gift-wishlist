# spring-gift-product

# 구현 기능

| URL                  | Method | Description             |
| -------------------- | ------ | ----------------------- |
| `/api/products`      | GET    | 제품 전체를 조회        |
| `/api/products`      | POST   | 제품을 추가             |
| `/api/products/{id}` | PUT    | 특정 상품의 내용을 수정 |
| `/api/products/{id}` | DELETE | 특정 상품 삭제          |

# Step2 관리자 화면

## 화면 구성

1. ### 전체 상품 리스트 확인 화면
   - 저장되어 있는 상품을 표 형식으로 화면에 볼 수 있게 한다.
   - 상품 등록 버튼을 만들어 상품 등록 화면으로 이동하게 한다.
   - 상품 마다 수정, 삭제 버튼을 만들어 수정폼으로 이동하거나 상품을 삭제 할 수 있게한다.
2. ### 상품 등록 화면
   - 상품의 이름, 가격, 이미지url을 받아서 등록 할 수있게 한다.
   - 가격은 숫자만 입력 가능하게 한다.
   - 등록이 완료 되면 전체 상품 리스트 확인 화면으로 돌아간다.
3. ### 상품 수정 화면
   - 전체 상품 리스트 확인 페이지에서 수정 버튼을 누르면 나온다.
   - 이전 상품 정보가 입력되어있고 수정하여 저장 할 수 있게한다.
   - 수정이 완료되면 전체 상품 리스트 확인 화면으로 돌아간다.

## 상세 구현 기능 및 방법

AdminProductController.java를 만들어서 관리자 페이지 응답 받음<br/>
templates 아래 admin폴더 안에 html 파일들을 생성함<br/>
static 아래 css 폴더 안에 css 파일들을 생성함<br/>

1. ### 상품 전체 목록 조회 기능

- 모든 상품의 id,이름,가격,이미지url를 "products" 이름으로 폼에 넘겨 화면에서 보여줌(id 제외)

2. ### 상품 등록 기능

- new ProductRequestDto()로 빈 product를 넘기고 폼에서 정보를 입력 받음.
- 폼에서 등록 버튼을 누르면 @PostMapping으로 이를 받아서 저장

3. ### 상품 수정 기능

- 원래 있던 상품의 id 값을 참고 하여 원래 정보를 화면에 띄어줌
- 이후 상품 등록과 마찬가지로 값을 입력받고 정보를 업데이트 함

4. ### 상품 삭제

- 삭제 버튼을 누르면 id가 전달되어 삭제됨

# Step3 데이터 베이스

h2 데이터베이스를 연결하고 jdbcclient를 사용하여 기존 컬렉션에 저장하던것을 데이터베이스에 저장한다.

## 구현 기능

1. schema.sql을 사용하여 테이블을 생성한다.
2. jdbcclient을 사용하여 ProductRepository에서 sql을 작성하여 데이터를 저장한다.
3. jdbcclient을 사용하여 ProductRepository에서 sql을 작성하여 데이터를 찾아온다.
4. jdbcclient을 사용하여 ProductRepository에서 sql을 작성하여 데이터를 수정한다.
5. jdbcclient을 사용하여 ProductRepository에서 sql을 작성하여 테이터를 삭제한다.
