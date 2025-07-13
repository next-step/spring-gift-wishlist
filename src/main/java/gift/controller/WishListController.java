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


    @GetMapping("/")
    public ResponseEntity<List<ProductResponseDto>> getWishlist(@AuthenticatedUser String token) {

        List<ProductResponseDto> products = wishListService.findAllProductsFromWishList(token);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/")
    public ResponseEntity<List<ProductResponseDto>> addProductToWishlist(@AuthenticatedUser String token,
                                                                         @Valid @RequestBody WishListProductRequestDto productRequestDto) {

        List<ProductResponseDto> products = wishListService.addProductToWishListByEmail(token, productRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductFromWishlist(@AuthenticatedUser String token,
                                                          @PathVariable("productId") Long productId) {

        wishListService.deleteProductFromWishList(token, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
