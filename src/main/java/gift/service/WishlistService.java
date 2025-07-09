package gift.service;

import gift.domain.Wishlist;
import gift.dto.wishlist.CreateWishlistRequest;
import gift.dto.wishlist.WishlistResponse;
import gift.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<WishlistResponse> getWishlistsByUserId(Long id) {
        return wishlistRepository.findAllByUserId(id);
    }

    public void deleteWishlist(Long userId, Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }
}
