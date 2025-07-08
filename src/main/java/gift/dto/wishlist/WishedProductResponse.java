package gift.dto.wishlist;

import gift.entity.WishedProduct;

public record WishedProductResponse(
        Long id,
        String name,
        Long price,
        String imageUrl,
        Integer quantity,
        Long subtotal
) {
    public static WishedProductResponse from(WishedProduct wishedProduct) {
        return new WishedProductResponse(
                wishedProduct.getId(),
                wishedProduct.getName(),
                wishedProduct.getPrice(),
                wishedProduct.getImageUrl(),
                wishedProduct.getQuantity(),
                wishedProduct.getSubtotal()
        );
    }
}
