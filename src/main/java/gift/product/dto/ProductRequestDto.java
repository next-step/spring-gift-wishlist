package gift.product.dto;

import gift.product.entity.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductRequestDto {

    private Long id;
    @Size(min = 1 , max = 15,message = "이름은 1~15자 여야합니다")
    private String name;
    @NotNull(message = "비울 수 없습니다")
    private Integer price;
    private String imageUrl;

    public static ProductRequestDto fromEntity(Product product) {

        ProductRequestDto dto = new ProductRequestDto();

        dto.id = product.getId();
        dto.name = product.getName();
        dto.price = product.getPrice();
        dto.imageUrl = product.getImageUrl();

        return dto;
    }

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
}
