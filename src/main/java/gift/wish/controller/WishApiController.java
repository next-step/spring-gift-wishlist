package gift.wish.controller;

import gift.auth.Login;
import gift.member.domain.Member;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishUpdateRequest;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishApiController {
    private final WishService wishService;

    public WishApiController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@Login Member member, @Valid @RequestBody WishRequest request) {
        wishService.addWish(member,request.productId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<WishListResponse>> getWishes(@Login Member member) {
        List<WishListResponse> wishes = wishService.getWishes(member);

        return ResponseEntity.ok(wishes);
    }

    @PutMapping
    public ResponseEntity<Void> updateWish(@Login Member member, @RequestBody WishUpdateRequest request) throws AccessDeniedException {
        wishService.updateQuantity(member, request.wishId(), request.quantity());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@Login Member member, @PathVariable Long wishId) throws AccessDeniedException {
        wishService.deleteWish(member, wishId);
        return ResponseEntity.noContent().build();
    }
}
