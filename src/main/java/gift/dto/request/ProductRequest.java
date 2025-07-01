package gift.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotBlank (message = "상품명은 필수입니다.")
        @Size (max=15, message = "상품명은 공백 포함 최대 15자까지 입력할 수 있습니다.")
        String name,

        @Positive (message = "상품 가격은 0보타 큰 값으로 입력해주세요")
        int price,

        @NotBlank (message = "상품 이미지를 등록해주세요.")
        String imageUrl
) {}
