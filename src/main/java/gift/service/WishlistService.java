package gift.service;

import gift.model.WishItem;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import java.util.Optional;
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

  public void addToWishlist(Long memberId, Long productId) {
    Optional<WishItem> existing = wishlistRepository.findByMemberIdAndProductId(memberId, productId);
    if (existing.isPresent()) {
      throw new IllegalArgumentException("이미 찜한 상품입니다.");
    }

    wishlistRepository.insert(memberId, productId);
  }

}

