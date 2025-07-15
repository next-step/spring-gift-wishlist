package gift.controller;

import gift.jwt.JwtUtil;
import gift.model.Product;
import gift.service.WishlistService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final JwtUtil jwtUtil;

    public  WishlistController(WishlistService wishlistService, JwtUtil jwtUtil) {
        this.wishlistService = wishlistService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getWishlist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        String userEmail = jwtUtil.extractEmailFromHeader(authorization);
        return ResponseEntity.ok(wishlistService.getWishlist(userEmail));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Product> AddWishProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, 
            @PathVariable Long productId){
        try {
            String userEmail = jwtUtil.extractEmailFromHeader(authorization);
            Product addedProduct = wishlistService.addProduct(userEmail, productId);
            return ResponseEntity.ok(addedProduct);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> DeleteWishProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, 
            @PathVariable Long productId){
        String userEmail = jwtUtil.extractEmailFromHeader(authorization);
        wishlistService.deleteProduct(userEmail, productId);
        return ResponseEntity.ok().build();

    }
}
