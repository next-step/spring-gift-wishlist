package gift.product.entity;

import gift.product.dto.ProductRequestDto;

public class Product {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Product(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, ProductRequestDto requestDto) {
        this.id = id;
        this.name = requestDto.name();
        this.price = requestDto.price();
        this.imageUrl = requestDto.imageUrl();
    }
}
