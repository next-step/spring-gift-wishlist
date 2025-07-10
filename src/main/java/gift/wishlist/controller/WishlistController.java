package gift.wishlist.controller;

import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.entity.Wishlist;
import gift.wishlist.service.WishlistService;
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

    @PostMapping
    public ResponseEntity<WishResponseDto> addWishlist(
            @RequestHeader Long memberId,
            @RequestBody WishRequestDto wishRequestDto) {
        WishResponseDto wishResponseDto =
                wishlistService.addWish(memberId, wishRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(wishResponseDto);
    }
}
