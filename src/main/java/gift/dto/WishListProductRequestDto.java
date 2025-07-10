package gift.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WishListProductRequestDto {

    private int id;
    @Size(min = 1, max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$",
            message = "문자 및 숫자 또는 유효한 특수문자 ( '( )', '[ ]', '+', '-', '&', '/', '_' ) 만 사용할 수 있습니다."
    )
    private String name;
    private Integer price;

    public WishListProductRequestDto() {}

    public WishListProductRequestDto(Integer id, String name, Integer price) {
        this.id =  id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
