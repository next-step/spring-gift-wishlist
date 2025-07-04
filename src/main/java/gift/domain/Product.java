package gift.domain;

import gift.exception.ProductDomainRuleException;

public class Product {

    public static final Long MAX_PRICE = 9999999999L;

    private final Long id;
    private final String name;
    private final Long price;
    private final String imageUrl;

    private Product(Long id, String name, Long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product of(Long id, String name, Long price, String imageUrl) {
        if (name == null || !name.matches("^[A-Za-z가-힣0-9()\\[\\]+\\-&/_ ]{1,15}$")) {
            throw new ProductDomainRuleException("상품명은 15자 이하의 영문, 한글, 숫자 및 특수기호 ()[]+-&/_만 허용됨: " + name);
        }
        if (price == null || price < 0 || MAX_PRICE < price) {
            throw new ProductDomainRuleException("상품 가격은 10자리 이하의 양수여야함: " + price);
        }
        return new Product(id, name, price, imageUrl);
    }

    public boolean involveKakao() {
        return name.matches(".*카카오.*");
    }

    public Product copy() {
        return new Product(id, name, price, imageUrl);
    }

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
}
