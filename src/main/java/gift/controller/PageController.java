package gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/auth")
    public String authPage() {
        return "auth";
    }

    @GetMapping("/wishlist")
    public String wishlistPage() {
        return "wishlist";
    }
}
