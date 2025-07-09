package gift.controller;

import gift.domain.Wish;
import gift.dto.WishResponse;
import gift.service.WishService;
import gift.domain.Member;
import gift.resolver.LoginMember;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public List<WishResponse> getWishes(@LoginMember Member member) {
        return wishService.getWishes(member.getId());
    }
}
