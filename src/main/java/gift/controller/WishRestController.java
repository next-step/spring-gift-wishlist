package gift.controller;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.dto.LoginMember;
import gift.entity.Product;
import gift.jwt.Authenticated;
import gift.service.WishService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishRestController {

    private final WishService wishService;

    public WishRestController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<CreateWishResponse> addWish(@Authenticated LoginMember member,
            @RequestBody CreateWishRequest request) {
        wishService.addWish(member.getId(), request.getProductId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWish(@Authenticated LoginMember member,
            @PathVariable Long productId) {
        wishService.removeWish(member.getId(), productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getMyWishes(@Authenticated LoginMember loginMember) {
        List<Product> wishes = wishService.getAllWish(loginMember.getId());
        return ResponseEntity.ok(wishes);
    }

}
