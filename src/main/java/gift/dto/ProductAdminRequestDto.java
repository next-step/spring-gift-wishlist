package gift.dto;


import gift.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductAdminRequestDto {

    @NotBlank(message = "상품 이름은 필수 입력 사항입니다.")
    @Size(max = 15, message = "상품 이름은 공백 포함 최대 15자까지 가능합니다.")
    @Pattern(
            regexp = "^[\\p{L}\\p{N} ()\\[\\]+\\-\\&/_]*$",
            message = "허용되지 않은 특수문자가 포함되어 있습니다."
    )
    private String name;

    @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
    private String imageUrl;

    public ProductAdminRequestDto() {
    }

    public ProductAdminRequestDto(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toEntity() {
        return new Product(name, price, imageUrl);
    }

    // Getter/Setter
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
