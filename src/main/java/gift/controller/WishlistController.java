package gift.controller;

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

import gift.dto.AddWishlistRequest;
import gift.dto.WishResponse;
import gift.resolver.LoginMemberId;
import gift.service.WishlistService;

@RestController
@RequestMapping("/api")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishService) {
        this.wishlistService = wishService;
    }

    @GetMapping("/wishes")
    public ResponseEntity<List<WishResponse>> getProductsFromWishlist(
        @LoginMemberId Long memberId
    ) {
        List<WishResponse> products = wishlistService.getProductsFromWishlist(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/wishes")
    public ResponseEntity<WishResponse> addProductToWishlist(
        @LoginMemberId Long memberId,
        @RequestBody AddWishlistRequest request
    ) {
        WishResponse response = wishlistService.addProductToWishlist(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/wishes/{productId}")
    public ResponseEntity<Void> deleteProductFromWishlist(
        @LoginMemberId Long memberId,
        @PathVariable Long productId
    ) {
        wishlistService.deleteProductFromWishlist(memberId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
