package gift.wish.controller;

import gift.global.resolver.LoginMember;
import gift.product.dto.ProductResponse;
import gift.wish.service.WishService;
import gift.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/wishes")
public class WishViewController {

    private final WishService wishService;

    public WishViewController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public String showWishList(@LoginMember Member member, Model model) {
        List<ProductResponse> wishes = wishService.getWishes(member);
        model.addAttribute("wishes", wishes);
        return "wishes/wishList";
    }
}
