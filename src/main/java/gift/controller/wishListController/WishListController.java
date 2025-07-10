package gift.controller.wishListController;


import gift.Jwt.TokenUtils;
import gift.dto.wishListDto.AddWishItemDto;
import gift.dto.wishListDto.ResponseWishItemDto;
import gift.service.wishListService.WishListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ResponseWishItemDto>> getWishItemList(@RequestHeader("Authorization") String authHeader, @RequestParam(required = false) String name, @RequestParam(required = false) Integer price) {
        String token = tokenUtils.extractToken(authHeader);

        tokenUtils.validateToken(token);

        String userEmail = tokenUtils.extractEmail(token);

        List<ResponseWishItemDto> wishItemList = wishListService.getItemList(name, price, userEmail);

        return new ResponseEntity<>(wishItemList, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ResponseWishItemDto> deleteWishItem(@RequestHeader("Authorization") String authHeader, @RequestParam String name) {
        String token = tokenUtils.extractToken(authHeader);

        tokenUtils.validateToken(token);

        String userEmail = tokenUtils.extractEmail(token);

        ResponseWishItemDto deletedWishItem = wishListService.deleteWishItem(name, userEmail);

        return new ResponseEntity<>(deletedWishItem, HttpStatus.NO_CONTENT);
    }
}
