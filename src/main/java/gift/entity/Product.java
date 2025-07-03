package gift.entity;

import gift.dto.ProductRequestDto;

public class Product {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public Product() {
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(ProductRequestDto productRequestDto) {
        this.name = productRequestDto.getName();
        this.price = productRequestDto.getPrice();
        this.imageUrl = productRequestDto.getImageUrl();
    }

    public static Product createWithId(Product productWithoutId, Long newId) {
        return new Product(
                newId,
                productWithoutId.getName(),
                productWithoutId.getPrice(),
                productWithoutId.getImageUrl()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
