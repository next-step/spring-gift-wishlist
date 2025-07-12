package gift.dto.response;

import gift.entity.Gift;

public record GiftResponse(
        Long id,
        Long giftId,
        String giftName,
        Integer giftPrice,
        String giftPhotoUrl
) {
    public static GiftResponse from(Gift gift) {
        return new GiftResponse(
                gift.getId(),
                gift.getGiftId(),
                gift.getGiftName(),
                gift.getGiftPrice(),
                gift.getGiftPhotoUrl()
        );
    }
}
