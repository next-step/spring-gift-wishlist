package gift.dto.request;

import gift.entity.Gift;
import jakarta.validation.constraints.Size;

public record RequestModifyGift(
        Long giftId,

        @Size(min = 1, max = 15, message = "")
        String giftName,
        Integer giftPrice,
        String giftPhotoUrl
){
    public static Gift toEntity(RequestModifyGift requestModifyGift){
        return new Gift(
                requestModifyGift.giftId(),
                requestModifyGift.giftName(),
                requestModifyGift.giftPrice(),
                requestModifyGift.giftPhotoUrl()
        );
    }
}
