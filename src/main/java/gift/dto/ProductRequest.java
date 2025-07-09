package gift.dto;

import gift.domain.Product;
import gift.domain.ProductStatus;
import gift.validator.ValidProductName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductRequest {

    @NotBlank(message = "상품 이름은 필수입니다.")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
    @ValidProductName
    private final String name;
    private final int price;
    private final String imageUrl;
    private final ProductStatus status;

    public ProductRequest(String name, int price, String imageUrl, ProductStatus status) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public Product toEntity() {
        return new Product(name, price, imageUrl,status);
    }
}
