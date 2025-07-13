package gift.wish.controller;

import gift.auth.Login;
import gift.member.domain.Member;
import gift.member.dto.MemberTokenRequest;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishUpdateRequest;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<Void> addWish(@Login MemberTokenRequest memberTokenRequest, @Valid @RequestBody WishRequest request) {
        wishService.addWish(memberTokenRequest ,request.productId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<WishListResponse>> getWishes(@Login MemberTokenRequest memberTokenRequest) {
        List<WishListResponse> wishes = wishService.getWishes(memberTokenRequest);

        return ResponseEntity.ok(wishes);
    }

    @PatchMapping("/{wishId}")
    public ResponseEntity<Void> updateWish(@Login MemberTokenRequest memberTokenRequest, @PathVariable Long wishId, @RequestBody WishUpdateRequest request){
        wishService.updateQuantity(memberTokenRequest, wishId, request.quantity());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@Login MemberTokenRequest memberTokenRequest, @PathVariable Long wishId){
        wishService.deleteWish(memberTokenRequest, wishId);
        return ResponseEntity.noContent().build();
    }
}
