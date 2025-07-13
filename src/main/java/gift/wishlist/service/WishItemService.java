package gift.wishlist.service;

import gift.member.repository.MemberRepository;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import gift.wishlist.domain.WishItem;
import gift.wishlist.dto.GetWishItemResponseDto;
import gift.wishlist.dto.RegisterWishItemRequestDto;
import gift.wishlist.exception.WishItemAlreadyExistsException;
import gift.wishlist.exception.WishItemNotFoundException;
import gift.wishlist.repository.WishItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishItemService {

  private final WishItemRepository wishItemRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  public WishItemService(WishItemRepository wishItemRepository, MemberRepository memberRepository,
      ProductRepository productRepository) {
    this.wishItemRepository = wishItemRepository;
    this.memberRepository = memberRepository;
    this.productRepository = productRepository;
  }

  @Transactional
  public Long registerWishItem(Long memberId, RegisterWishItemRequestDto dto) {
    if (memberRepository.findById(memberId).isEmpty()) {
      throw new IllegalArgumentException("회원이 존재하지 않습니다.");
    }
    if (productRepository.findById(dto.productId()).isEmpty()) {
      throw new ProductNotFoundException();
    }
    if (wishItemRepository.findByMemberIdAndProductId(memberId, dto.productId()).isPresent()) {
      throw new WishItemAlreadyExistsException();
    }
    return wishItemRepository.save(WishItem.of(memberId, dto.productId()));
  }

  public List<GetWishItemResponseDto> findWishItems(Long memberId) {
    if (memberRepository.findById(memberId).isEmpty()) {
      throw new IllegalArgumentException("회원이 존재하지 않습니다.");
    }
    return wishItemRepository.findWishItemsWithProductByMemberId(memberId);
  }

  @Transactional
  public void deleteWishItem(Long id) {
    if (wishItemRepository.findById(id).isEmpty()) {
      throw new WishItemNotFoundException();
    }
    wishItemRepository.deleteById(id);
  }

}
