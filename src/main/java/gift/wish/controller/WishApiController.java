package gift.wish.controller;

import gift.product.dto.ProductResponse;
import gift.wish.dto.WishRequest;
import gift.member.entity.Member;
import gift.global.resolver.LoginMember;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishApiController {

    private final WishService wishService;

    public WishApiController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getWishes(@LoginMember Member member) {
        List<ProductResponse> wishes = wishService.getWishes(member);
        return ResponseEntity.ok(wishes);
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@LoginMember Member member, @Valid @RequestBody WishRequest request) {
        wishService.addWish(member, request.productId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWish(@LoginMember Member member, @PathVariable("productId") Long productId) {
        wishService.deleteWish(member, productId);
        return ResponseEntity.noContent().build();
    }
}