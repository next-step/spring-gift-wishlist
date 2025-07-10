package gift.controller;

import gift.dto.WishlistItemRequestDto;
import gift.dto.WishlistItemResponseDto;
import gift.service.WishlistService;
import jakarta.validation.Valid;
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

    @GetMapping("/{memberId}")
    public ResponseEntity<List<WishlistItemResponseDto>> findAllWishlistItemsByMemberId(
            @PathVariable Long memberId
    ) {
        List<WishlistItemResponseDto> items = wishlistService.findAllWishlistItemsByMemberId(memberId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/{memberId}")
    public ResponseEntity<Void> addWishlistItem(
            @PathVariable Long memberId,
            @Valid @RequestBody WishlistItemRequestDto requestDto
    ) {
        wishlistService.addWishlistItem(memberId, requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<Void> updateWishlistItemById(
            @PathVariable Long itemId,
            @RequestParam Long quantity
    ) {
        wishlistService.updateWishlistItemById(itemId, quantity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteWishlistItemById(
            @PathVariable Long itemId
    ) {
        wishlistService.deleteWishlistItemById(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
