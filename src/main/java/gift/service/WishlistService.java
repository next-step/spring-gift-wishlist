package gift.service;

import gift.domain.Wishlist;
import gift.dto.wishlist.CreateWishlistRequest;
import gift.repository.WishlistRepository;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public Wishlist saveWishlist(Long userId, CreateWishlistRequest request) {
        Wishlist wishlist = new Wishlist(userId, request.productId());
        return wishlistRepository.save(wishlist);
    }
}
