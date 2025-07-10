package gift.wishlist.service;

import gift.exception.ProductNotFoundException;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.entity.Wishlist;
import gift.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    public WishResponseDto addWish(Long memberId, WishRequestDto wishRequestDto) {
        Product product = productRepository.findById(wishRequestDto.productId())
                .orElseThrow(() -> new ProductNotFoundException(wishRequestDto.productId()));

        Wishlist wishlist = wishlistRepository.saveWish(
                memberId,
                wishRequestDto.productId(),
                wishRequestDto.quantity()
        );

        return new WishResponseDto(
                wishlist.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                wishlist.getQuantity()
        );
    }
}
