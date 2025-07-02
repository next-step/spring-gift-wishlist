package gift.domain.product.dto;

import jakarta.validation.constraints.*;

public class ProductRequest {
    @NotBlank(message = "상품 이름은 비어 있을 수 없습니다.")
    @Size(max = 15, message = "상품명은 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s()\\[\\]+&/_-]*$",
            message = "상품명에는 허용되지 않은 특수문자가 포함되어 있습니다. 사용 가능한 특수문자: ( ), [ ], +, -, &, /, _")
    @Pattern(regexp = "^(?!.*카카오).*$",
            message = "\"카카오\"가 포함된 상품명은 담당 MD와 협의 후 사용 가능합니다.")
    private String name;

    @NotNull(message = "상품 가격은 비어 있을 수 없습니다.")
    @Min(value = 1, message = "상품 가격은 1 이상의 값이어야 합니다.")
    private int price;

    @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
    private String imageUrl;

    public ProductRequest() {
    }
    
    public ProductRequest(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
}
