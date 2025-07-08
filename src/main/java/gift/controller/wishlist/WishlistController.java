package gift.controller.wishlist;

import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.service.auth.AuthService;
import gift.service.wishlist.WishlistService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    
    private final AuthService authService;
    private final WishlistService wishlistService;
    
    public WishlistController(AuthService authService, WishlistService wishlistService) {
        this.authService = authService;
        this.wishlistService = wishlistService;
    }
    
    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> findMyWishlist(
        @RequestHeader("Authorization") String token
    ) {
        Long userId = authService.checkPermissonForUser(token);
        List<WishlistResponseDto> myWishlist = wishlistService.findMyWishlistByUserId(userId);
        return new ResponseEntity<>(myWishlist, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<WishlistResponseDto> addToMyWishlist(
        @RequestHeader("Authorization") String token,
        @RequestBody WishlistRequestDto requestDto
    ) {
        Long userId = authService.checkPermissonForUser(token);
        WishlistResponseDto responseDto = wishlistService.addToMyWishlist(userId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteToMyWishlist(
        @RequestHeader("Authorization") String token,
        @RequestBody WishlistRequestDto requestDto
    ) {
        Long userId = authService.checkPermissonForUser(token);
        wishlistService.deleteFromMyWishlist(userId, requestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /*
    @PatchMapping
    public ResponseEntity<WishlistReponseDto> modifyProductCntFromMyWishlist(
        @RequestHeader("Authorization") String token,
        @RequestBody WishlistRequestDto requestDto
    ) {
        Long userId = authService.checkPermissonForUser(token);
        WishlistReponseDto reponseDto = wishlistService.modifyProductCntFromMyWishlist(userId, requestDto);
        return new ResponseEntity<WishlistReponseDto>(reponseDto, HttpStatus.OK);
    }
    */
}
