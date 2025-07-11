package gift.service;

import gift.dto.*;

import java.util.List;

public interface MemberServiceInterface {

    boolean isEmailExists(String email);

    MemberResponseDto register(MemberRequestDto requestDto);

    MemberResponseDto login(MemberRequestDto requestDto);

    List<ProductResponseDto> findAllProductsFromWishList(String token);

    List<ProductResponseDto> addProductToWishListByEmail(String token, WishListProductRequestDto requestDto);

    void deleteProductFromWishList(String token, Long productId);
}
