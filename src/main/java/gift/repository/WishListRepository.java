package gift.repository;

import gift.domain.WishList;
import gift.dto.WishListResponseDto;
import java.util.List;
import java.util.Optional;

public interface WishListRepository {

  WishListResponseDto saveWishList(WishList wishList);

  Optional<WishListResponseDto> searchByMemberAndProduct(Long memberId, Long productId);

  List<WishListResponseDto> searchAllByMemberId(Long memberId);

  WishListResponseDto updateQuantity(Long memberId, Long productId, int quantity);

  void deleteByMemberAndProduct(Long memberId, Long productId);

  void deleteAllByMemberId(Long memberId);
}
