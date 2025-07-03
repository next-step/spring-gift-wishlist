package gift.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequest(
    @NotNull(message = "상품명은 필수입니다.")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-&/_\\uAC00-\\uD7AF]+$", message = "허용되지 않은 특수 문자가 포함되었습니다.")
    String name,
    @NotNull(message = "가격은 필수입니다.")
    Integer price,
    String imageUrl
) {

    @AssertTrue(message = "상품명에 '카카오'가 포함되었습니다. 담당자와 협의가 필요합니다.")
    public boolean isKakaoValid() {
        return ((name == null) || !(name.contains("카카오")));
    }

}