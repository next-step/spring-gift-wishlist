package gift.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WishListProductRequestDto {

    private Long productId;

    public WishListProductRequestDto() {}

    public WishListProductRequestDto(Long productId) {
        this.productId =  productId;
    }

    public Long getproductId() {
        return productId;
    }
}
