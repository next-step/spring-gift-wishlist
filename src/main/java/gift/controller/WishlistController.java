package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.WishlistRequestDTO;
import gift.dto.WishlistResponseDTO;
import gift.dto.WishlistUpdateRequestDTO;
import gift.entity.Member;
import gift.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<WishlistResponseDTO> addWishlist(
            @Valid @RequestBody WishlistRequestDTO request,
            @LoginMember Member member) {
        WishlistResponseDTO response = wishlistService.addWishlist(member.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponseDTO>> getWishlist(@LoginMember Member member) {
        List<WishlistResponseDTO> wishlists = wishlistService.getAllWishlistByMemberId(member.getId());
        return ResponseEntity.ok(wishlists);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishlistResponseDTO> updateWishlist(
            @PathVariable Integer id,
            @Valid @RequestBody WishlistUpdateRequestDTO request,
            @LoginMember Member member) {
        WishlistResponseDTO response = wishlistService.updateQuantity(id, request.quantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(
            @PathVariable Integer id,
            @LoginMember Member member) {
        wishlistService.deleteWishlist(id);
        return ResponseEntity.noContent().build();
    }
}
