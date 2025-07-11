package gift.wishlist;

import gift.auth.LoginUser;
import gift.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Wishlist>> getWishlist(@LoginUser User user) {
        List<Wishlist> wishlist = wishlistService.getWishlistById(user.getId());
        return ResponseEntity.ok(wishlist);
    }
}
