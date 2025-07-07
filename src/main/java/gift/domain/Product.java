package gift.domain;

import gift.validation.ProductNameValidator;
import gift.validation.ProductPriceValidator;

public class Product {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private Product(Long id, String name, Long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    /*
    [질문]
    이름에 "카카오"가 포함된 상품은 MD와 협의된 경우에만 허용된다고 조건으로 나와 있습니다.
    현재 Product.of(...) 에서 무조건 ProductNameValidator.validateName()을 호출하게 되어 있는데,
    상품 자체의 조건으로 간주되어 위와 같은 코드로 Product를 생성할 때, 상품 이름에 "카카오"가 포함되어 있으면, validateName()에서 예외가 발생하여 어떤 경우에도 Product를 생성할 수 없지 않나요??
    그럼 Validator.validateName() 에서 MD와 협의된 경우인지 확인하는 로직을 작성해야 되는건가요??
     */
    public static Product of(Long id, String name, Long price, String imageUrl) {
        ProductNameValidator.validateName(name);
        ProductPriceValidator.validatePrice(price);
        return new Product(id, name, price, imageUrl);
    }
}
