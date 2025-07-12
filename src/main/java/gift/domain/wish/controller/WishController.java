package gift.domain.wish.controller;

import gift.domain.wish.dto.WishListResponse;
import gift.domain.wish.dto.WishRequest;
import gift.domain.wish.dto.WishResponse;
import gift.domain.wish.dto.WishUpdateRequest;
import gift.domain.wish.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<Void> createWish(
            @RequestBody WishRequest wishRequest,
            @RequestHeader("Authorization") String accessToken
    ) {
        wishService.createWish(wishRequest, accessToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWish(
            @PathVariable Long id,
            @RequestBody WishUpdateRequest wishUpdateRequest,
            @RequestHeader("Authorization") String accessToken
    ) {
        wishService.updateWish(id, wishUpdateRequest, accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WishResponse> deleteWish(
            @PathVariable Long id,
            @RequestHeader("Authorization") String accessToken
    ) {
        wishService.deleteWish(id, accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<WishListResponse> getWishes(
            @RequestHeader("Authorization") String accessToken
    ) {
        return new ResponseEntity<>(new WishListResponse(wishService.getWishes(accessToken)), HttpStatus.OK);
    }
}
