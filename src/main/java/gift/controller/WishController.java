package gift.controller;

import gift.config.LoginMember;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Member;
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
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<WishResponse>> getWishlist(@LoginMember Member member) {
        List<WishResponse> wishlist = wishService.getWishlist(member);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WishResponse> addToWishlist(
        @RequestBody
        WishRequest request,
        @LoginMember
        Member member
    ) {
        WishResponse response = wishService.addToWishlist(request, member);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(
        @PathVariable
        Long productId,
        @LoginMember
        Member member
    ) {
        wishService.removeFromWishlist(productId, member);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
