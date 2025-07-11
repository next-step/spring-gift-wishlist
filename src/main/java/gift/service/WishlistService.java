package gift.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.dto.ProductResponse;
import gift.repository.WishlistRepository;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Transactional
    public List<ProductResponse> getProductsFromWishlist(Long memberId) {
        if (!wishlistRepository.existsByMemberId(memberId)) {
            return List.of(); // 레코드가 하나도 없을 경우, 바로 빈 리스트를 반환!
        }

        return wishlistRepository.findAllProductByMemberId(memberId)
            .stream()
            .map(ProductResponse::from)
            .toList();
    }
}
