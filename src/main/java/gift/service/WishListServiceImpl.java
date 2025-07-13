package gift.service;

import gift.domain.WishList;
import gift.dto.WishListCreateRequestDto;
import gift.dto.WishListResponseDto;
import gift.dto.WishListUpdateRequestDto;
import gift.exception.WishListItemNotFoundException;
import gift.repository.WishListRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishListServiceImpl implements WishListService {

  private final Integer ZERO_CNT = 0;

  private final WishListRepository wishListRepository;
  private final ProductService productService;

  public WishListServiceImpl(WishListRepository wishListRepository, ProductService productService) {
    this.wishListRepository = wishListRepository;
    this.productService = productService;
  }

  private void validateProductExists(Long productId) {
    productService.searchProductById(productId);
  }

  @Override
  public WishListResponseDto addToWishList(Long memberId, WishListCreateRequestDto wishListCreateRequestDto) {
    Long productId = wishListCreateRequestDto.productId();
    Integer quantity = wishListCreateRequestDto.quantity();

    validateProductExists(productId);

    int quantityToAdd = quantity;

    return wishListRepository.searchByMemberAndProduct(memberId, productId)
        .map(current -> {
          int newQuantity = current.quantity() + quantityToAdd;
          return wishListRepository.updateQuantity(memberId, productId, newQuantity);
        })
        .orElseGet(() -> {
          WishList wishList = new WishList(memberId, productId, quantityToAdd);
          return wishListRepository.saveWishList(wishList);
        });
  }

  @Override
  public List<WishListResponseDto> getWishList(Long memberId) {
    return wishListRepository.searchAllByMemberId(memberId);
  }

  @Override
  public WishListResponseDto updateQuantity(Long memberId, WishListUpdateRequestDto wishListUpdateRequestDto) {
    Long productId = wishListUpdateRequestDto.productId();
    Integer quantity = wishListUpdateRequestDto.quantity();

    validateProductExists(productId);

    wishListRepository.searchByMemberAndProduct(memberId, productId)
        .orElseThrow(() -> new WishListItemNotFoundException(memberId, productId));

    if (quantity == ZERO_CNT) {
      wishListRepository.deleteByMemberAndProduct(memberId, productId);
      return new WishListResponseDto(memberId, productId, ZERO_CNT);
    }

    return wishListRepository.updateQuantity(memberId, productId, quantity);
  }

  @Override
  public void removeFromWishList(Long memberId, Long productId) {
    validateProductExists(productId);

    wishListRepository.searchByMemberAndProduct(memberId, productId)
        .orElseThrow(() -> new WishListItemNotFoundException(memberId, productId));

    wishListRepository.deleteByMemberAndProduct(memberId, productId);
  }

  @Override
  public void clearWishList(Long memberId) {
    wishListRepository.deleteAllByMemberId(memberId);
  }
}
