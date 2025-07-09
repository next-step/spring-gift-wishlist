package gift.controller;

import gift.common.argumentResolver.LoginUser;
import gift.dto.user.UserInfo;
import gift.dto.wishlist.CreateWishlistRequest;
import gift.dto.wishlist.WishlistResponse;
import gift.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistApiController {

    private final WishlistService wishlistService;

    public WishlistApiController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<Void> createWishlist(@LoginUser UserInfo userInfo, @RequestBody @Valid CreateWishlistRequest request) {
        wishlistService.saveWishlist(userInfo.id(), request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlists(@LoginUser UserInfo userInfo) {
        List<WishlistResponse> responses = wishlistService.getWishlistsByUserId(userInfo.id());
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@LoginUser UserInfo userInfo, @PathVariable(name = "id") Long wishlistId) {
        wishlistService.deleteWishlist(userInfo.id(), wishlistId);
        return ResponseEntity.noContent().build();
    }
}
