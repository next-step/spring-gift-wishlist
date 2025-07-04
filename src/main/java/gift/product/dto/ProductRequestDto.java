package gift.product.dto;

import gift.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequestDto {

    @Size(min = 1 , max = 15,message = "이름은 1~15자 여야합니다")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$", message = "특수문자는 (), [], +, -, &, /, _ 만 허용됩니다.")
    private String name;

    @NotBlank
    @NotNull(message = "비울 수 없습니다")
    private Integer price;
    private String imageUrl;

    public static ProductRequestDto fromEntity(Product product) {

        ProductRequestDto dto = new ProductRequestDto();

        dto.name = product.getName();
        dto.price = product.getPrice();
        dto.imageUrl = product.getImageUrl();

        return dto;
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
}
