package gift.front.controller;

import gift.api.service.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/members")
public class MemberFrontController {

    private final ProductService productService;

    public MemberFrontController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/login")
    public String loginMember() {
        return "member/login";
    }

    @GetMapping("/register")
    public String registerMember() {
        return "member/register";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Authorization 쿠키를 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/members/login";
    }

    @GetMapping("/products")
    public String allProducts(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) Long categoryId,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }

        model.addAttribute("products", productService.findAllProducts(pageable, categoryId));
        model.addAttribute("page", pageable.getPageNumber());

        return "member/product-list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(
            @PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findProductById(id));

        return "member/product-detail";
    }
}
