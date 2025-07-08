package gift.controller.wishlist;

import gift.config.annotation.CurrentUser;
import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.service.wishlist.WishlistService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    
    private final WishlistService wishlistService;
    
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }
    
    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> findMyWishlist(
        @CurrentUser Long userId
    ) {
        List<WishlistResponseDto> myWishlist = wishlistService.findMyWishlistByUserId(userId);
        return new ResponseEntity<>(myWishlist, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<WishlistResponseDto> addToMyWishlist(
        @CurrentUser Long userId,
        @RequestBody WishlistRequestDto requestDto
    ) {
        WishlistResponseDto responseDto = wishlistService.addToMyWishlist(userId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteToMyWishlist(
        @CurrentUser Long userId,
        @RequestBody WishlistRequestDto requestDto
    ) {
        wishlistService.deleteFromMyWishlist(userId, requestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    @PatchMapping
    public ResponseEntity<WishlistResponseDto> modifyProductCntFromMyWishlist(
        @CurrentUser Long userId,
        @RequestBody WishlistRequestDto requestDto
    ) {
        WishlistResponseDto responseDto = wishlistService.modifyProductCntFromMyWishlist(userId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
