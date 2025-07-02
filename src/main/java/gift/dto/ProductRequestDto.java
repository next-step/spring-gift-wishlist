package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequestDto {

    @NotBlank(message = "상품명을 입력해주세요")
    @Size(max = 15, message = "공백 포함 1 ~ 15자까지 입력이 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "특수문자는 ( ) [ ] + - & / _ 만 허용됩니다.")
    private String name;

    private int price;
    private String imageUrl;

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
