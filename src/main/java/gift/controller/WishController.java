package gift.controller;

import gift.config.LoginMember;
import gift.dto.WishCreateResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishlistService;

    public WishController(WishService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishlist(@LoginMember Member member) {
        List<WishResponseDto> wishlist = wishlistService.getWishlist(member.getId());
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping
    public ResponseEntity<WishCreateResponseDto> addWishlist(
            @RequestBody WishRequestDto requestDto,
            @LoginMember Member member
    ) {
        WishCreateResponseDto responseDto = wishlistService.add(member.getId(), requestDto.productId());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeWishlist(
            @PathVariable Long productId,
            @LoginMember Member member
    ) {
        wishlistService.remove(member.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}