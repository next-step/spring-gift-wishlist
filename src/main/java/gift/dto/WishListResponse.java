package gift.dto;

import gift.repository.projection.WishListWithProduct;
import java.util.List;

public record WishListResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        Integer quantity) {

    public static WishListResponse from(WishListWithProduct wishList) {
        return new WishListResponse(
                wishList.id(),
                wishList.product().name(),
                wishList.product().price(),
                wishList.product().imageUrl(),
                wishList.quantity()
        );
    }

    public static List<WishListResponse> fromList(List<WishListWithProduct> wishLists) {
        return wishLists.stream()
                .map(WishListResponse::from)
                .toList();
    }
}
