package gift.wishlist;

import gift.auth.LoginUser;
import gift.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping("/add")
    public ResponseEntity<Wishlist> addWishlist(@RequestBody WishlistSaveRequestDto wishlistSaveRequestDto) {
        Wishlist wishlist1 =  wishlistService.saveWishlist(wishlistSaveRequestDto);
        return ResponseEntity
                .created(URI.create("/api/product/" + wishlist1.getId()))
                .body(wishlist1);
    }
}
