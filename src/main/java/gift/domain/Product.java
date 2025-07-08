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

    public static Product of(Long id, String name, Long price, String imageUrl) {
        ProductNameValidator.validateName(name);
        ProductPriceValidator.validatePrice(price);
        return new Product(id, name, price, imageUrl);
    }
}
