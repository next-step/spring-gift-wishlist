package gift.wish.service;

import gift.exception.BusinessException;
import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import gift.wish.dao.WishDao;
import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WishService {
  private final ProductService productService;
  private final WishDao wishDao;

  public WishService(ProductService productService, WishDao wishDao) {
    this.productService = productService;
    this.wishDao = wishDao;
  }

  public WishResponseDto createWish(Long memberId, WishRequestDto wishRequestDto) {
    productService.findProductById(wishRequestDto.productId());

    return wishDao.createWish(memberId, wishRequestDto.productId());
  }

  public List<ProductResponseDto> getWishes(Long memberId) {
    List<Long> productIds = wishDao.findProductIdsByMemberId(memberId);
    return productIds.stream()
        .map(productService::findProductById)
        .collect(Collectors.toList());
  }

  public void deleteWish(Long memberId, Long productId) {
    wishDao.deleteWish(memberId, productId);
  }
}
