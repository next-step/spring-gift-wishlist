package gift.controller;

import gift.model.Product;
import gift.model.User;
import gift.service.WishlistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public  WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public List<Product> getWishlist(@AuthenticationPrincipal User user){
        return wishlistService.getWishlist(user.getEmail());
    }

    @PostMapping("/{productId}")
    public void AddWishProduct(@AuthenticationPrincipal User user, @PathVariable Long productId){
        System.out.println(user.getEmail());
        wishlistService.addProduct(user.getEmail(),productId);
    }

    @DeleteMapping("/{productId}")
    public void DeleteWishProduct(@AuthenticationPrincipal User user, @PathVariable Long productId){
        wishlistService.deleteProduct(user.getEmail(),productId);
    }

}
