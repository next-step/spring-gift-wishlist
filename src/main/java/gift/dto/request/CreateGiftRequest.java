package gift.dto.request;

import gift.entity.Gift;
import jakarta.validation.constraints.Size;

public record CreateGiftRequest(
        Long giftId,
        @Size(min = 1, max = 15, message = "상품 명은 공백포함 15자 이하여야 합니다.")
        String giftName,
        Integer giftPrice,
        String giftPhotoUrl
) {
    public static Gift toEntity (CreateGiftRequest createGiftRequest) {
        return new Gift(
                createGiftRequest.giftId(),
                createGiftRequest.giftName(),
                createGiftRequest.giftPrice(),
                createGiftRequest.giftPhotoUrl()
        );
    }
}
