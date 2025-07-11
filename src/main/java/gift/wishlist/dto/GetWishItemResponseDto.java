package gift.wishlist.dto;

import gift.product.domain.Product;

public record GetWishItemResponseDto(
    Long productId,
    String productName,
    int price,
    String imageUrl

) {

  public static GetWishItemResponseDto from(Product product) {
    return new GetWishItemResponseDto(product.id(), product.name(), product.price(),
        product.imageUrl());
  }

}
