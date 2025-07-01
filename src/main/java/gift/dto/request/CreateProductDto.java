package gift.dto.request;

import gift.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateProductDto(
        @NotBlank(message = "Product name is required") String name,
        @Min(value = 0, message = "Product price must be positive") Long price,
        String imageUrl
) {
    public Product toEntity() {
        return new Product(null, name, price, imageUrl);
    }
}
