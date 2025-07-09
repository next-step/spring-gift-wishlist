package gift.controller;

import gift.common.argumentResolver.LoginUser;
import gift.dto.user.UserInfo;
import gift.dto.wishlist.CreateWishlistRequest;
import gift.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
