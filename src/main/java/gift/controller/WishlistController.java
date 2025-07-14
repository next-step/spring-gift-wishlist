package gift.controller;

import gift.domain.Member;
import gift.domain.Wish;
import gift.resolver.LoginMember;
import gift.service.WishService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {
    private final WishService wishService;


    public WishlistController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public void add(@RequestBody Wish request, @LoginMember Member member) {
        wishService.addWish(member, request.getProductId());
    }

    @GetMapping
    public List<Wish> list(@LoginMember Member member) {
        return wishService.getWishes(member);
    }

    @DeleteMapping
    public void delete(@RequestBody Wish request, @LoginMember Member member) {
        wishService.deleteWish(member, request.getProductId());
    }
}
