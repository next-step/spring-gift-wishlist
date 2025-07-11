package gift.wishlist;

import gift.auth.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishlistService {
    private final WishlistDao wishlistDao;
    private final JwtProvider jwtProvider;

    public WishlistService(WishlistDao wishlistDao, JwtProvider jwtProvider) {
        this.wishlistDao = wishlistDao;
        this.jwtProvider = jwtProvider;
    }

    public List<Wishlist> getWishlistById(UUID userId) {
        return wishlistDao.getWishlistByUserId(userId);
    }
}
