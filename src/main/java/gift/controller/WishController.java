package gift.controller;

import gift.dto.WishCreateResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.service.WishService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<WishResponseDto>> getWishlist(
            HttpServletRequest request
    ) {
        Long memberId = Long.parseLong(request.getAttribute("memberId").toString());
        List<WishResponseDto> wishlist = wishlistService.getWishlist(memberId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping
    public ResponseEntity<WishCreateResponseDto> addWishlist(
            @RequestBody WishRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long memberId = Long.parseLong(request.getAttribute("memberId").toString());
        WishCreateResponseDto responseDto = wishlistService.add(memberId, requestDto.productId());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeWishlist(@PathVariable Long productId, HttpServletRequest request) {
        Long memberId = Long.parseLong(request.getAttribute("memberId").toString());
        wishlistService.remove(memberId, productId);
        return ResponseEntity.noContent().build();
    }
}