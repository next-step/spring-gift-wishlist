package gift.controller;

import gift.auth.LoginMember;
import gift.domain.Member;
import gift.domain.Product;
import gift.dto.WishRequest;
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

    @GetMapping
    public List<Product> getMyWishes(@LoginMember Member member) {
        return wishService.getWishlist(member.getId());
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishService.addWish(member.getId(), request.getProductId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeWish(@PathVariable Long productId, @LoginMember Member member) {
        if (wishService.removeWish(member.getId(), productId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
