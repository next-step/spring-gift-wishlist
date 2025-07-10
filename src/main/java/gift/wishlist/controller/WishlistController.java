package gift.wishlist.controller;

import gift.common.security.AuthenticatedMember;
import gift.common.security.LoginMember;
import gift.wishlist.dto.WishlistAddDto;
import gift.wishlist.dto.WishlistResponseDto;
import gift.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<WishlistResponseDto> addWishlist(
        @RequestBody @Valid WishlistAddDto wishlistAddDto,
        @LoginMember AuthenticatedMember member
    ) {
        WishlistResponseDto wishlistResponseDto = wishlistService.add(member.id(), wishlistAddDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistResponseDto);
    }

    @GetMapping("/{wishlistId}")
    public ResponseEntity<WishlistResponseDto> findWishlist(@PathVariable Long wishlistId) {
        WishlistResponseDto wishlistResponseDto = wishlistService.findWishlist(wishlistId);
        return ResponseEntity.status(HttpStatus.OK).body(wishlistResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> findAllWishlists() {
        List<WishlistResponseDto> wishlistResponseDtos = wishlistService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(wishlistResponseDtos);
    }

}
