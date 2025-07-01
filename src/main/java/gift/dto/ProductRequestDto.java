package gift.dto;

import jakarta.validation.constraints.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

public class ProductRequestDto {

    @NotBlank
    @Size(min = 1, max = 15)
    @Pattern(
            regexp = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-\\&/_]*$",
            message = "이름에는 ( ) [ ] + - & / _ 외의 특수문자를 사용할 수 없습니다."
    )
    private String name;

    @NotNull
    private Long price;

    @NotBlank
    private String imageUrl;

    private boolean kakaoWordAllow = false;


    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @AssertTrue(message = "'카카오'라는단어는담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    public boolean isKakaoWordAllowed(){
        if (!kakaoWordAllow && name.contains("카카오")){
            return false;
        }
        return true;
    }
}
