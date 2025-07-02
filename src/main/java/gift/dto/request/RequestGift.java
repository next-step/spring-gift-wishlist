package gift.dto.request;

import gift.entity.Gift;
import jakarta.validation.constraints.Size;

public record RequestGift(
        Long giftId,
        @Size(min = 1, max = 15, message = "상품 명은 공백포함 15자 이하여야 합니다.")
        String giftName,
        Integer giftPrice,
        String giftPhotoUrl
) {
    public static Gift toEntity (RequestGift requestGift) {
        return new Gift(
                requestGift.giftId(),
                requestGift.giftName(),
                requestGift.giftPrice(),
                requestGift.giftPhotoUrl()
        );
    }
}
