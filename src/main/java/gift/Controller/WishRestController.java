package gift.Controller;

import gift.Entity.Member;
import gift.Entity.Product;
import gift.annotation.LoginMember;
import gift.dto.WishDto;
import gift.dto.WishRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishRestController {

    private final WishDto wishDto;

    public WishRestController(WishDto wishDto) {
        this.wishDto = wishDto;
    }

    @GetMapping
    public List<Product> getWishes(@LoginMember Member member) {
        return wishDto.findWishesByMember(member.getId());
    }

    @PostMapping
    public void addWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishDto.insertWish(member.getId(), request.getProductId());
    }

    @DeleteMapping
    public void removeWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishDto.deleteWish(member.getId(), request.getProductId());
    }
}

