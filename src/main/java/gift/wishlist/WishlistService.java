package gift.wishlist;

import gift.common.exception.NoSuchIdException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WishlistService {
    private final WishlistDao wishlistDao;

    public WishlistService(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    @Transactional
    public List<Wishlist> getWishlistById(UUID userId) {
        return wishlistDao.getWishlistByUserId(userId);
    }

    @Transactional
    public Wishlist saveWishlist(UUID id, WishlistSaveRequestDto wishlistSaveRequestDto) {
        Wishlist wishlist = new Wishlist(id, wishlistSaveRequestDto.getProductId());
        return wishlistDao.save(wishlist);
    }

    @Transactional
    public void deleteWishlist(Long id) {
        if(wishlistDao.findById(id).isEmpty()) {
            throw new NoSuchIdException("존재하지 않는 ID입니다.");
        }
        wishlistDao.delete(id);
    }
}
