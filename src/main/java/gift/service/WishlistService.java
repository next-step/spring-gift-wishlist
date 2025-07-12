package gift.service;

import gift.dto.response.GiftResponse;
import gift.entity.Wishlist;
import gift.repository.GiftRepository;
import gift.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final GiftRepository giftRepository;

    public WishlistService(
            WishlistRepository wishlistRepository,
            GiftRepository giftRepository
    ) {
        this.wishlistRepository = wishlistRepository;
        this.giftRepository = giftRepository;
    }

    public List<GiftResponse> getWishlists(Long userId){
        List<Wishlist> wishlists = wishlistRepository.getWishList(userId);
        List<GiftResponse> giftResponses = new ArrayList<>();
        for (Wishlist wishlist : wishlists) {
            giftRepository.findById(wishlist.getGiftId()).ifPresent(giftResponse -> {
                giftResponses.add(GiftResponse.from(giftResponse));
            });
        }
        return giftResponses;
    }

    public void addWishList(Long giftId, Long userId){
        wishlistRepository.addWishList(new Wishlist(giftId, userId));
    }

    public void deleteWishlist(Long giftId, Long userId){
        wishlistRepository.deleteWishList(new Wishlist(giftId, userId));
    }
}
