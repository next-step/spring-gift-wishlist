package gift.controller;

import gift.auth.LoginMember;
import gift.dto.api.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public List<WishResponseDto> getWishList(@LoginMember Member member) {
        return wishService.getWishListForMember(member);
    }

}
