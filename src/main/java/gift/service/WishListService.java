package gift.service;

import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishSummaryResponseDto;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    public WishListService(WishListRepository wishListRepository, ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
    }

    public List<WishSummaryResponseDto> findAllWishSummaryByMemberId(Long memberId) {

        return wishListRepository.findAllWishSummaryByMemberId(memberId)
                .stream()
                .map(WishSummaryResponseDto::from)
                .toList();
    }

    public void saveWish(Long memberId, Long productId) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(productId);

        wishListRepository.saveWish(new Wish(memberId, findProduct.getId()));
    }

    public void deleteWish(Long memberId, Long productId) {
        Product findProduct = productRepository.findProductByIdOrElseThrow(productId);

        wishListRepository.deleteWish(new Wish(memberId, findProduct.getId()));
    }
}
