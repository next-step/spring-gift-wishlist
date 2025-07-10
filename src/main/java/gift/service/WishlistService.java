package gift.service;

import gift.model.WishItem;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {
  private final WishlistRepository wishlistRepository;

  public WishlistService(WishlistRepository wishlistRepository) {
    this.wishlistRepository = wishlistRepository;
  }

  public WishItem getWishItem(Long memberId, Long productId) {
    return wishlistRepository.findByMemberIdAndProductId(memberId, productId)
        .orElseThrow(() -> new RuntimeException("찜한 상품을 찾을 수 없습니다"));
  }

  // ✅ 전체 찜 목록 조회
  public List<WishItem> getWishList(Long memberId) {
    return wishlistRepository.findAllByMemberId(memberId);
  }
}

