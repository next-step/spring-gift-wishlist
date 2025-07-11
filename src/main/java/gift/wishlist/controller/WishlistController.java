package gift.wishlist.controller;

import gift.common.annotation.LogInMember;
import gift.wishlist.dto.WishAddRequest;
import gift.wishlist.dto.WishResponse;
import gift.wishlist.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<WishResponse> addWish(
        @RequestBody WishAddRequest wishAddRequest,
        @LogInMember Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(wishlistService.addWish(wishAddRequest, memberId));
    }
}
