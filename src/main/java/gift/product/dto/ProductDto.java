package gift.product.dto;

import gift.product.domain.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductDto {
    @NotNull
    @Size(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Z가-힣()\\[\\]\\+\\-&/_ ]*$", message = "영어, 한글, (, ), [, ], +, -, &, /, _ 만 입력 가능합니다")
    private String name;
    private int price;
    private String imageUrl;

    public ProductDto() {}

    public ProductDto(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
