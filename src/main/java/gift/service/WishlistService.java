package gift.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.domain.Product;
import gift.dto.AddWishlistRequest;
import gift.dto.WishResponse;
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

    @Transactional(readOnly = true)
    public List<WishResponse> getProductsFromWishlist(Long memberId) {
        return wishlistRepository.findAllProductByWishlistId(memberId)
            .stream()
            .map(WishResponse::from)
            .toList();
    }

    @Transactional
    public WishResponse addProductToWishlist(Long memberId, AddWishlistRequest request) {
        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));

        int count = wishlistRepository.addProductToWishlist(memberId, request.productId());
        if (count != 1) {
            throw new WishlistAddException("위시리스트 상품 추가를 실패했습니다.");
        }

        // return ProductResponse.from(product);
        return new WishResponse(
            product.getId(),
            product.getPrice(),
            product.getName(),
            product.getImageUrl(),
            1L
        );
    }

    @Transactional
    public void deleteProductFromWishlist(Long memberId, Long productId) {
        int count = wishlistRepository.deleteProductFromWishlist(memberId, productId);
        if (count != 1) {
            throw new WishlistDeleteException("위시리스트 상품 삭제를 실패했습니다.");
        }
    }
}
