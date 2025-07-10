package gift.dto.request;

import gift.entity.Gift;
import jakarta.validation.constraints.Size;

public record GiftCreateRequest(
        Long giftId,
        @Size(min = 1, max = 15, message = "상품 명은 공백포함 15자 이하여야 합니다.")
        String giftName,
        Integer giftPrice,
        String giftPhotoUrl
) {
    public Gift toEntity () {
        return new Gift(
                this.giftId(),
                this.giftName(),
                this.giftPrice(),
                this.giftPhotoUrl()
        );
    }
}
