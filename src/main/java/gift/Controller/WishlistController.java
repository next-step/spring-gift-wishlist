package gift.Controller;

import gift.model.Member;
import gift.model.WishItem;
import gift.service.WishlistService;
import gift.util.LoginMember;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/wishlist")
public class WishlistController {

  private final WishlistService wishlistService;

  public WishlistController(WishlistService wishlistService) {
    this.wishlistService = wishlistService;
  }

  // ✅ 전체 찜 목록 조회
  @GetMapping
  public String getWishlist(@LoginMember Member member, Model model) {
    List<WishItem> wishList = wishlistService.getWishList(member.getId());
    model.addAttribute("wishList", wishList);
    return "wishlist/list"; // list.html
  }

  // ✅ 개별 찜 항목 조회
  @GetMapping("/{productId}")
  public String getWishItem(@PathVariable Long productId,
      @LoginMember Member member,
      Model model) {
    WishItem wishItem = wishlistService.getWishItem(member.getId(), productId);
    model.addAttribute("wishItem", wishItem);
    return "wishlist/detail"; // detail.html
  }
}

