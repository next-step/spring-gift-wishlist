
# 개발 일기

어느날에 무엇을 어떤 근거로 개발했는지를 적음.
까먹지 않기 위해 알은 것들, 알아야하는 것들을 적음.

### 2025.07.04 ~ 2025.07.07
##### 적용 : 사용자 어노테이션
- `@Target(ElementType.TYPE)`  
    클래스·인터페이스·열거형 등에만 붙일 수 있도록 범위를 제한
- `@Retention(RetentionPolicy.RUNTIME)`
    런타임 시에도 이 애노테이션 정보를 유지해, 리플렉션 가능
- `@Documented`  
    Javadoc 생성 시 이 애노테이션을 문서화하여 표시하도록 합니다.
##### 적용 : 포트와 어댑터 아키텍처인 헥사고날아키텍처

비스니스 로직(Domain)을 외부 기술(Web,DB)와 완전히 분리한다.
Domain은 데이터모델과 모델의 비지니스 규칙을 넣음
Application은 중간계층으로 실제 기능을 담당하고,
port(인터페이스)와 useCase(구현체)로 나뉨.
port는 어플리케이션이 무엇을 할 수 있는가를 적고, useCase는 그에 대한 구현체임.
port는 in과 out으로 나뉨.
들어오는 포트인 in은 비지니스 로직 입장에서 client인 controller가 사용하는 포트.
나가는 포트인 out은 비지니스 로직이 client로서 외부기능(DB)를 사용하기 위한 포트.

##### 적용 : 컨트롤러에 수많은 useCase 주입?
Controller에 ProductUseCase를 하나 주입받도록 만드는게 나은지 아니면 AddProductUseCase, .... 각각 이렇게 주입하는게 맞을지 고민했다.
결과는 SOLID 원칙의 ISP원칙(클라이언트는 자신이 사용하지 않는 메서드에 의존하도록 강요받으면 안됨)을 근거로 2안을 택함.
단점은 보일러플레이트 코드의 증가지만 추후 lombok을 사용하면 완화 가능하다고 생각했음.

##### 적용 : ProductMapper
DDD에서 개념적으로 비지니스로직은 도메인계층에 두어야 한다(실무적인지 아닌지는 PASS)
비지니스 로직은 이런 것이라고 생각한다. 
Dto Entity 매핑은 비지니스로직이 아니라 단순한 기술적 관심사로 보고 도메인과 분리했다.

##### 개념 : 헥사고날 아키텍처
input port = primary port = Driving port = Inbound port
어플리케이션을 호출 즉, 작동시키는 쪽, 또는 불러들이는 쪽이라 이렇게 부름

output port = secondary port = Driven port = outbound Port
DB와 같은 외부 자원을 사용하므로 이렇게 부름

infrastructure layer
구현체들이 있는 곳을 말한다.
인터페이스가 아닌 기술적인 내용이 있는 곳이다. 
http, jdbc와 분리되어있다.

인프라스트럭처 레이어는 어플리케이션과 도메인을 의존함. . 어댑터가 위치하고, Spring Controller와 jdbcClient 같은 구체적인 기술을 사용.

어플리케이션 레이어는 도메인을 의존함. 인프라에는 의존하지 않으며 포트를 사용함.

도메인은 아무것도 의존하지 않음. 비지니스 로직이 위치해야 함.

##### 적용 : 헥사고날 아키텍처 구조 적용

Client ──HTTP──▶
🔵  Primary Adapter              (Infrastructure Layer)
    ProductController            @RestController  
    └─ calls **Input Port / Use-case**

   ▼
🟢  Application Layer
    • AddProductUseCase (interface)  
    • ProductInteractor (impl) ─ orchestrates Domain & Ports

   ▼
🟡  Domain Layer
    • Product (Entity)  
    • Value Objects  (아직 필요x)
    • ProductDomainService (아직 필요X)

   ▼
🔴  Output Port                  (Application Layer)
    ProductPersistencePort (interface)

   ▼
🔵  Secondary Adapter            (Infrastructure Layer)
    ProductPersistenceAdapter    @Repository  
    └─ JDBC

현재 비지니스 로직이라고 판단될만 한 것이 없다고 생각하여 도메인에 비지니스 로직이 위치하지 않음.  기본 CRUD와 Dto<->Entity 변환은 비지니스로직이 아니라고 생각했음
##### 개념 : 전통적인 계층 vs DDD/CA
전통적인 계층은 서비스 계층에 비지니스 로직을 두지만,
DDD/CA(도메인 드라이븐 디자인, 클린아키텍처)에서는 도메인 계층에 비지니스 로직을 둔다.
##### 개념 : SOLID 원칙이란

- S - 단일 책임 원칙 (Single Responsibility Principle)
> 클래스는 오로지 하나의 변경 이유만을 가져야 한다.

- O - 개방-폐쇄 원칙 (Open/Closed Principle)
> 소프트웨어 개체(클래스, 모듈 등)는 확장에는 열려 있어야 하고, 수정에는 닫혀 있어야 한다.

- L - 리스코프 치환 원칙 (Liskov Substitution Principle)
> 하위 타입은 언제나 자신의 기반(부모) 타입으로 교체될 수 있어야 한다.

- I - 인터페이스 분리 원칙 (Interface Segregation Principle)
> 클라이언트는 자신이 사용하지 않는 메서드에 의존하도록 강요받아서는 안 된다.

- D - 의존성 역전 원칙 (Dependency Inversion Principle)
> 상위 수준 모듈은 하위 수준 모듈에 의존해서는 안 되며, 둘 모두 추상화(인터페이스)에 의존해야 한다.