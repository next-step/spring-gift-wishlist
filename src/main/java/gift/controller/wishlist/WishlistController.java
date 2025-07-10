package gift.controller.wishlist;

import gift.config.annotation.CurrentUser;
import gift.config.annotation.ValidHeader;
import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.service.wishlist.WishlistService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @ValidHeader
    public ResponseEntity<List<WishlistResponseDto>> findMyWishlist(
        @CurrentUser Long userId
    ) {
        List<WishlistResponseDto> myWishlist = wishlistService.findMyWishlistByUserId(userId);
        return new ResponseEntity<>(myWishlist, HttpStatus.OK);
    }
    
    @PostMapping
    @ValidHeader
    public ResponseEntity<WishlistResponseDto> addToMyWishlist(
        @CurrentUser Long userId,
        @RequestBody WishlistRequestDto requestDto
    ) {
        WishlistResponseDto responseDto = wishlistService.addToMyWishlist(userId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{productId}")
    @ValidHeader
    public ResponseEntity<Void> deleteToMyWishlist(
        @CurrentUser Long userId,
        @PathVariable(name = "productId") Long id
    ) {
        wishlistService.deleteFromMyWishlist(userId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    @PatchMapping
    @ValidHeader
    public ResponseEntity<?> modifyProductCntFromMyWishlist(
        @CurrentUser Long userId,
        @RequestBody WishlistRequestDto requestDto
    ) {
        WishlistResponseDto responseDto = wishlistService.modifyProductCntFromMyWishlist(userId, requestDto);
        if(responseDto == null) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
