package gift.product.dto;

import gift.global.annotation.ImageURLConstraint;
import gift.product.annotation.ProductNameConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductUpdateRequest {

    @Size(min = 1, max = 15, message = "상품이름은 1글자 이상 15글자 이하여야합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$",
            message = "( ), [ ], +, -, &, /, _ 외의 특수문자는 사용할 수 없습니다.")
    @ProductNameConstraint
    private String name;

    @Min(value = 1, message = "0원 이하는 가격으로 설정할 수 없습니다.")
    private int price;

    @ImageURLConstraint
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
