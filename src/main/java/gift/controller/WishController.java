package gift.controller;

import gift.dto.WishResponseDto;
import gift.service.WishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<WishResponseDto> addWish(@RequestParam Long memberId, @RequestParam Long productId) {
        WishResponseDto created = wishService.createWish(memberId, productId);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long wishId) {
        wishService.deleteWishById(wishId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishes(@RequestParam Long memberId) {
        List<WishResponseDto> result = wishService.findAllWishesByMemberId(memberId);
        return ResponseEntity.ok(result);
    }
}
