package gift.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.dto.AddWishlistRequest;
import gift.dto.ProductResponse;
import gift.exception.ProductNotFoundException;
import gift.exception.WishlistAddException;
import gift.exception.WishlistDeleteException;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(
        WishlistRepository wishlistRepository,
        ProductRepository productRepository
    ) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
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

    @Transactional
    public ProductResponse addProductToWishlist(Long memberId, AddWishlistRequest request) {
        if (!productRepository.existsById(request.productId())) {
            throw new ProductNotFoundException("해당 상품이 존재하지 않습니다.");
        }

        int count = wishlistRepository.addProductToWishlist(memberId, request.productId());
        if (count != 1) {
            throw new WishlistAddException("위시리스트 상품 추가를 실패했습니다.");
        }

        return productRepository.findById(request.productId())
            .map(ProductResponse::from)
            .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));
        // 43L에서 상품 id의 유효성을 이미 검증했지만, productRepository.findById()의 리턴이 optional이므로..
    }

    @Transactional
    public void deleteProductFromWishlist(Long memberId, Long productId) {
        int count = wishlistRepository.deleteProductFromWishlist(memberId, productId);
        if (count != 1) {
            throw new WishlistDeleteException("위시리스트 상품 삭제를 실패했습니다.");
        }
    }
}
