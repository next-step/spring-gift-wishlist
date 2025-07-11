package gift.service;

import gift.dto.ProductResponseDto;
import gift.entity.Wishlist;
import gift.repository.WishlistRepository;
import gift.service.ProductService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    public WishlistService(WishlistRepository wishlistRepository,
            ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
    }

    public List<ProductResponseDto> getWishlist(Long memberId) {
        List<Wishlist> list = wishlistRepository.findByMemberId(memberId);
        return list.stream()
                .map(w -> productService.findProduct(w.getProductId()))
                .collect(Collectors.toList());
    }

    public void addToWishlist(Long memberId, Long productId) {
        Optional<Wishlist> existing = wishlistRepository.findByMemberIdAndProductId(memberId, productId);
        if (existing.isPresent()) {
            Wishlist wishlist = existing.get();
            wishlist.setQuantity(wishlist.getQuantity() + 1);
            wishlistRepository.update(wishlist);
        } else {
            Wishlist wishlist = new Wishlist(memberId, productId);
            wishlistRepository.save(wishlist);
        }
    }


    public void removeFromWishlist(Long memberId, Long productId) {
        wishlistRepository.findByMemberIdAndProductId(memberId, productId)
                .ifPresent(w -> wishlistRepository.deleteByMemberIdAndProductId(memberId, productId));
    }
}