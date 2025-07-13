package gift.service;

import gift.dto.request.WishAddRequestDto;
import gift.dto.request.WishDeleteRequestDto;
import gift.dto.request.WishUpdateRequestDto;
import gift.dto.response.WishIdResponseDto;
import gift.entity.WishProduct;
import java.util.List;

public interface WishService {

    WishIdResponseDto addProduct(WishAddRequestDto wishAddRequestDto, String email);

    List<WishProduct> getWishList(String email);

    void deleteProduct(String email, Long wishId, WishDeleteRequestDto wishDeleteRequestDto);

    void updateProduct(Long wishId, String email, WishUpdateRequestDto wishUpdateRequestDto);
}
