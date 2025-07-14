package gift.Controller;

import gift.Entity.Member;
import gift.Entity.Product;
import gift.annotation.LoginMember;
import gift.dto.WishDao;
import gift.dto.WishRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishRestController {

    private final WishDao wishDao;

    public WishRestController(WishDao wishDao) {
        this.wishDao = wishDao;
    }

    @GetMapping
    public List<Product> getWishes(@LoginMember Member member) {
        return wishDao.findWishesByMember(member.getId());
    }

    @PostMapping
    public void addWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishDao.insertWish(member.getId(), request.getProductId());
    }

    @DeleteMapping
    public void removeWish(@RequestBody WishRequest request, @LoginMember Member member) {
        wishDao.deleteWish(member.getId(), request.getProductId());
    }
}

