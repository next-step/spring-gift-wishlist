package gift.service.wishlist;

import gift.dto.product.ProductResponseDto;
import gift.dto.wishlist.WishRequestDto;
import java.util.List;

public interface WishListService {
    public void create(WishRequestDto requestDto, Long memberId);

    List<ProductResponseDto> findAll(Long memberId);
}