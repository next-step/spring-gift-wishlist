package gift.dto;

import gift.validation.ForbiddenWord;
import jakarta.validation.constraints.*;

public class ProductRequestDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력값입니다.")
    @Size(max = 15, message = "상품명은 15자 이하로 입력해주세요.")
    @Pattern(
            regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-\\&/_\\s]*$",
            message = "특수문자는 ()[]+-&/_ 만 사용할 수 있어요."
    )
    @ForbiddenWord(word = "카카오", message = "상품명에 <카카오>가 포함된 상품은 담당 MD에게 문의해주세요.")
    private String name;

    @NotNull(message = "가격은 필수 입력값입니다.")
    @Min(value = 100, message = "가격은 100원 이상으로 등록해주세요.")
    private Integer price;

    private String imageUrl;

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setPrice(Integer price) { this.price = price; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl;}

    public Long getId() { return id; }

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
