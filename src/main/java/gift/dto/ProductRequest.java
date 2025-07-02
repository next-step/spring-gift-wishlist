// src/main/java/gift/dto/ProductRequest.java
package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotBlank(message = "상품 이름은 필수 입력값입니다.")
        @Size(max=15, message = "상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.")
        @Pattern(regexp = "^[\\p{IsHangul}\\p{IsHan}\\p{IsAlphabetic}\\d\\s()\\[\\]+\\-&/_]+$",
                message = "상품 이름에 허용되지 않는 문자가 포함되었습니다.")
        String name,
        @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
        int price,
        @NotBlank(message = "이미지 URL은 필수 입력값입니다.")
        String imageUrl
) {

}
