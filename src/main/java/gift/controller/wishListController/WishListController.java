package gift.controller.wishListController;


import gift.Jwt.JwtUtil;
import gift.Jwt.TokenUtils;
import gift.dto.itemDto.ItemCreateDto;
import gift.dto.wishListDto.AddWishItemDto;
import gift.dto.wishListDto.ResponseWishItemDto;
import gift.service.itemService.ItemService;
import gift.service.wishListService.WishListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wish")
public class WishListController {

    private final WishListService wishListService;
    private final TokenUtils tokenUtils;

    public WishListController(WishListService wishListService, TokenUtils tokenUtils) {
        this.wishListService = wishListService;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping
    public ResponseEntity<ResponseWishItemDto> addItem(@RequestBody @Valid AddWishItemDto dto, @RequestHeader("Authorization") String authHeader) {
        String token = tokenUtils.extractToken(authHeader);

        tokenUtils.validateToken(token);

        String userEmail = tokenUtils.extractEmail(token);

        ResponseWishItemDto addedWishItem = wishListService.addWishItem(dto, userEmail);

        return new ResponseEntity<>(addedWishItem, HttpStatus.CREATED);
    }

}
