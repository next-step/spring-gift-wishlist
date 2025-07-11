package gift.controller;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import gift.auth.LoginMember;
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
    public void addWish(@RequestBody WishRequestDto requestDto, @LoginMember Member member) {
        wishService.addWish(member.getId(), requestDto.getProductId());
    }

    @GetMapping
    public List<WishResponseDto> getWishes(@LoginMember Member member) {
        return wishService.getWishes(member.getId());
    }

    @DeleteMapping("/{productId}")
    public void removeWish(@PathVariable Long productId, @LoginMember Member member) {
        wishService.removeWish(member.getId(), productId);
    }
}
