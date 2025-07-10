package gift.service.wishlist;

import gift.dto.product.ProductResponseDto;
import java.util.List;

public interface WishListService {
    public void create(Long productId, Long memberId);

    List<ProductResponseDto> findAll(Long memberId);
}