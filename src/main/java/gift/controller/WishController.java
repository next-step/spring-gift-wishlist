package gift.controller;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.service.WishService;
import gift.auth.LoginMember;
import gift.auth.LoginMemberInfoDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public void addWish(@RequestBody WishRequestDto requestDto, @LoginMember LoginMemberInfoDto member) {
        wishService.addWish(member.id(), requestDto.productId());
    }

    @GetMapping
    public List<WishResponseDto> getWishes(@LoginMember LoginMemberInfoDto member) {
        return wishService.getWishes(member.id());
    }

    @DeleteMapping("/{wishId}")
    public void removeWish(@PathVariable Long wishId, @LoginMember LoginMemberInfoDto member) {
        wishService.removeWish(member.id(), wishId);
    }
}
