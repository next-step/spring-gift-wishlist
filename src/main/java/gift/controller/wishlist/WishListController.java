package gift.controller.wishlist;

import gift.dto.wishlist.WishRequestDto;
import gift.entity.LoginMember;
import gift.entity.Member;
import gift.service.wishlist.WishListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping
    public ResponseEntity<Void> create(
        @RequestBody WishRequestDto requestDto,
        @LoginMember Member member
    ) {
        wishListService.create(requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}