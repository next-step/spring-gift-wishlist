package gift.wish.controller;

import gift.auth.Login;
import gift.member.domain.Member;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishUpdateRequest;
import gift.wish.service.WishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/wishes")
public class WishController {
    private WishService wishService;

    public WishController(WishService wishService){
        this.wishService = wishService;
    }

    @GetMapping
    public String getWishes(@Login Member member, Model model) {
        List<WishListResponse> wishes = wishService.getWishes(member);

        model.addAttribute("wishes", wishes);
        return "wishes/wishes";
    }

    @PostMapping("/add")
    public String addWish(@Login Member member, @ModelAttribute WishRequest request) {
        wishService.addWish(member, request.productId());
        return "redirect:/wishes";
    }

    @PostMapping("/update/{wishId}")
    public String updateWish(@Login Member member, @PathVariable Long wishId, @ModelAttribute WishUpdateRequest request){
        wishService.updateQuantity(member, wishId, request.quantity());

        return "redirect:/wishes";
    }

    @PostMapping("/delete/{wishId}")
    public String deleteWish(@Login Member member, @PathVariable("wishId") Long wishId) {
        wishService.deleteWish(member, wishId);
        return "redirect:/wishes";
    }
}
