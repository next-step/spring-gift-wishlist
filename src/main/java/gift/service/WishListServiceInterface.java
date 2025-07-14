package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishListProductRequestDto;

import java.util.List;

public interface WishListServiceInterface {
    List<ProductResponseDto> findAllProductsFromWishList(String token);

    List<ProductResponseDto> addProductToWishListByEmail(String token, WishListProductRequestDto requestDto);

    void deleteProductFromWishList(String token, Long productId);
}
