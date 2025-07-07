package gift.product.dto;

import gift.product.domain.Product;

public class ResponseDto {
    private String name;
    private int price;
    private String imageUrl;

    public ResponseDto() {}

    public ResponseDto(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ResponseDto(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }

    public String getName() { return name; }

    public Integer getPrice() { return price; }

    public String getImageUrl() { return imageUrl; }
}
