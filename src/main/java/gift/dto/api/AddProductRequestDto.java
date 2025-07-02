package gift.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AddProductRequestDto(
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,15}$",
        message = "이름은 한글, 영문, 숫자, 공백, (), [], +, -, &, /, _ 만 포함할 수 있으며 최대 15자까지 입력 가능합니다."
    )
    @NotNull(message = "이름은 필수입니다.")
    String name,
    @NotNull(message = "가격은 필수입니다.")
    Long price,
    @NotNull(message = "이미지 url은 필수입니다.")
    String imageUrl,
    @NotNull(message = "MD 협의 여부는 필수입니다.")
    Boolean mdOk
) {
    
    public AddProductRequestDto(String name, Long price, String imageUrl, Boolean mdOk) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdOk = mdOk;
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public Long price() {
        return price;
    }
    
    @Override
    public String imageUrl() {
        return imageUrl;
    }
    
    @Override
    @JsonProperty("mdOk")
    public Boolean mdOk() {
        return mdOk;
    }
    
    public Boolean goodName() {
        if (name.contains("카카오")) {
            return mdOk;
        }
        return true;
    }
}
