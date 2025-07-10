package gift.controller;

import gift.domain.Product;
import gift.dto.*;
import gift.service.WishService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishRestController {
    private final WishService service;


    public WishRestController(WishService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public HttpEntity<List<Product>> getProducts() {
        List<Product> productList = service.productList();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PostMapping()
    public HttpEntity<CreateWishResponse> addWishList(@Validated @RequestBody CreateWishRequest request) {
        CreateWishResponse response = service.addWishProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    public HttpEntity<List<WishResponse>> getWishList(@PathVariable Long memberId) {
        List<WishResponse> wishList = service.getMeberWishList(memberId);
        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }

    @DeleteMapping("/{wishId}")
    HttpEntity<Void> deleteProduct(@PathVariable Long wishId) {
        service.delete(wishId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{wishId}")
    HttpEntity<UpdateWishResponse> updateQuantity(@Validated @RequestBody UpdateWishRequest request, @PathVariable Long wishId) {
        UpdateWishResponse response = service.updateQuantity(request, wishId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
