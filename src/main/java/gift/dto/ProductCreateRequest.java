package gift.dto;

import gift.entity.Product;

public record ProductCreateRequest(
    String name,
    Long price,
    String imageUrl
) {

    public ProductCreateRequest(String name, Long price, String imageUrl) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
        this.name = name;
        if (price == null) {
            throw new IllegalArgumentException("상품 가격은 필수입니다.");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("상품 가격은 0보다 커야 합니다.");
        }
        this.price = price;
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("상품 이미지 URL은 필수입니다.");
        }
        this.imageUrl = imageUrl;
    }

    public Product toProduct() {
        return new Product(null, name, price, imageUrl);
    }
}
