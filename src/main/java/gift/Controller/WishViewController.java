package gift.Controller;

import gift.Entity.Member;
import gift.Entity.Product;
import gift.annotation.LoginMember;
import gift.dto.WishDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/wishes")
public class WishViewController {

    private final WishDao wishDao;

    public WishViewController(WishDao wishDao) {
        this.wishDao = wishDao;
    }

    // 위시리스트 보기
    @GetMapping
    public String showWishList(@LoginMember Member member, Model model) {
        if (member == null){
            throw new RuntimeException("로그인이 되어있지 않습니다.");
        }

        List<Product> wishProducts = wishDao.findWishesByMember(member.getId());
        model.addAttribute("wishProducts", wishProducts);
        return "products/wishlist";
    }

    // 위시리스트에 추가
    @PostMapping("/{id}/wish")
    public String addWish(@PathVariable Long id, @LoginMember Member member) {
        wishDao.insertWish(member.getId(), id);
        return "redirect:/user/products";
    }

    // 위시리스트에서 제거
    @PostMapping("/{id}/delete")
    public String removeWish(@PathVariable Long id, @LoginMember Member member) {
        wishDao.deleteWish(member.getId(), id);
        return "redirect:/user/products";
    }
}


