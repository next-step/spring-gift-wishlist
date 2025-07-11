package gift.controller;

import gift.config.LoginMember;
import gift.dto.request.WishRequestDto;
import gift.entity.Member;
import gift.service.WishService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public void addWish(@RequestBody WishRequestDto wishRequestDto, @LoginMember Member member) {
        wishService.addWish(member.getId(), wishRequestDto.getProductId(),wishRequestDto.getQuantity());
    }
}
