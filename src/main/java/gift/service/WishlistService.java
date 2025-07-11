package gift.service;

import gift.exception.InvalidQuantityException;
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
    Optional<WishItem> existing = wishlistRepository.findByMemberIdAndProductId(memberId, productId);
    if(existing.isEmpty()) {
      throw new IllegalArgumentException("찜 항목이 존재하지 않습니다");
    }
    return existing.get();
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

  // 찜 상품 수량 조절
  public void updateQuantity(Long memberId, Long productId, int quantity) {
    if (quantity < 1) {
      throw new InvalidQuantityException("수량은 1 이상이어야 합니다.");
    }
    wishlistRepository.updateQuantity(memberId, productId, quantity);
  }

  public void deleteWishListItem(Long memberId, Long productId) {
    wishlistRepository.delete(memberId, productId);
  }

}

