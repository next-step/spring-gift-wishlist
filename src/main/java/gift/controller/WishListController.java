package gift.controller;

import gift.annotation.AuthenticatedUser;
import gift.dto.ProductResponseDto;
import gift.dto.WishListProductRequestDto;
import gift.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }


    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getWishlist(@AuthenticatedUser String email) {

        List<ProductResponseDto> products = wishListService.findAllProductsFromWishList(email);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping
    public ResponseEntity<List<ProductResponseDto>> addProductToWishlist(@AuthenticatedUser String email,
                                                                         @Valid @RequestBody WishListProductRequestDto productRequestDto) {

        List<ProductResponseDto> products = wishListService.addProductToWishListByEmail(email, productRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductFromWishlist(@AuthenticatedUser String email,
                                                          @PathVariable("productId") Long productId) {

        wishListService.deleteProductFromWishList(email, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
