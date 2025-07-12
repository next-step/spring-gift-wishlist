package gift.controller;

import gift.dto.ProductResponse;
import gift.dto.WishRequest;
import gift.entity.Member;
import gift.resolver.LoginMember;
import gift.service.WishService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<ProductResponse>> getWishes(@LoginMember Member member) {
        List<ProductResponse> wishes = wishService.getWishes(member);
        return ResponseEntity.ok(wishes);
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@LoginMember Member member, @Valid @RequestBody WishRequest request) {
        wishService.addWish(member, request.productId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWish(@LoginMember Member member, @PathVariable("productId") Long productId) {
        wishService.deleteWish(member, productId);
        return ResponseEntity.noContent().build();
    }
}