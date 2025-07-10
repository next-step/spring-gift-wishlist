package gift.controller;

import gift.LoginMember;
import gift.dto.WishListResponseDto;
import gift.entity.Member;
import gift.service.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wish-list")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<List<WishListResponseDto>> getWishList(@LoginMember Member member) {
        List<WishListResponseDto> wishList = wishListService.getWishListByMemberId(member.getId());
        return ResponseEntity.ok(wishList);
    }
}
