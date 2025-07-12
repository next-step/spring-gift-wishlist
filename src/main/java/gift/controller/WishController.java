package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.Member;
import gift.domain.Product;
import gift.dto.WishRequest;
import gift.service.WishService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public List<Product> getWishes(@LoginMember Member member) {
        return wishService.getWishProducts(member.getId());
    }

    @PostMapping
    public void addWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishService.addWish(member.getId(), request.getProductId());
    }

    @DeleteMapping
    public void removeWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishService.removeWish(member.getId(), request.getProductId());
    }
}
