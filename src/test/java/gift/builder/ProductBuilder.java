package gift.builder;

import gift.entity.Product;

public class ProductBuilder {

    private String name = "기본 상품";
    private Double price = 4500.0;
    private String imageUrl = "http://default.img";

    private ProductBuilder() {

    }

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withPrice(Double price) {
        this.price = price;
        return this;
    }

    public ProductBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Product build() {
        return new Product(name, price, imageUrl);
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}