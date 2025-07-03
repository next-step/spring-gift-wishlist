package gift.product.dto;

import gift.product.annotation.ProductNameConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductUpdateRequest {

    @Size(min = 1, max = 15, message = "상품이름은 1글자 이상 15글자 이하여야합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$",
            message = "( ), [ ], +, -, &, /, _ 외의 특수문자는 사용할 수 없습니다.")
    @ProductNameConstraint(message = "카카오가 포함된 문구는 담당 MD와 협의 후 사용할 수 있습니다.")
    private String name;

    @Min(value = 1, message = "0원 이하는 가격으로 설정할 수 없습니다.")
    private int price;

    @NotBlank(message = "이미지 url은 필수 값입니다.")
    private String imageURL;

    protected ProductUpdateRequest() {}

    public ProductUpdateRequest(String name, int price, String imageURL) {
        this.name = name;
        this.price = price;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }
}
