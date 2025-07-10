package gift.wishlist.controller;

import gift.global.annotation.LoginMember;
import gift.member.entity.Member;
import gift.wishlist.dto.WishRequest;
import gift.wishlist.dto.WishResponse;
import gift.wishlist.service.WishlistService;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<List<WishResponse>> getWishes(@LoginMember Member member) {
        List<WishResponse> wishes = wishlistService.getWishes(member);
        return ResponseEntity.ok(wishes);
    }

    @PostMapping
    public ResponseEntity<String> addWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishlistService.addWish(request, member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{itemId}")
    public void deleteWish(@PathVariable("itemId") Long itemId, @LoginMember Member member) {
        wishlistService.deleteWish(itemId, member);
    }
}