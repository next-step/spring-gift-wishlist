package gift.wishlist;

import gift.auth.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishlistService {
    private final WishlistDao wishlistDao;

    public WishlistService(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    public List<Wishlist> getWishlistById(UUID userId) {
        return wishlistDao.getWishlistByUserId(userId);
    }

    public Wishlist saveWishlist(WishlistSaveRequestDto wishlistSaveRequestDto) {
        Wishlist wishlist = new Wishlist(null, wishlistSaveRequestDto.getUserId(), wishlistSaveRequestDto.getProductId());
        return wishlistDao.save(wishlist);
    }
}
