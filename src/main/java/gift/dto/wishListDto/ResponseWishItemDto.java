package gift.dto.wishListDto;

import gift.entity.WishItem;

public record ResponseWishItemDto(String name, String imageUrl, Integer price, Integer quantity) {
    public static ResponseWishItemDto from(WishItem wishItem) {
        return new ResponseWishItemDto(wishItem.name(), wishItem.imageUrl(), wishItem.price(), wishItem.quantity());
    }
}
