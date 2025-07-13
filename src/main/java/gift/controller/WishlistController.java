package gift.controller;

import gift.annotation.AuthUser;
import gift.dto.response.GiftResponse;
import gift.dto.response.UserResponse;
import gift.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping()
    public ResponseEntity<List<GiftResponse>> getWishLists(@AuthUser UserResponse user) {
        return ResponseEntity.ok().body(wishlistService.getWishlists(user.id()));
    }

    @PostMapping("/{giftId}")
    public ResponseEntity<String> addWishList(@AuthUser UserResponse user, @PathVariable Long giftId) {
        wishlistService.addWishList(giftId, user.id());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{giftId}")
    public ResponseEntity<String> deleteWishList(@AuthUser UserResponse user, @PathVariable Long giftId) {
        wishlistService.deleteWishlist(giftId, user.id());
        return ResponseEntity.noContent().build();
    }
}
