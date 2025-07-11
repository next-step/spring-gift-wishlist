package gift.wish.service;

import gift.product.service.ProductService;
import gift.wish.dao.WishDao;
import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
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

}
