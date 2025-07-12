package gift.controller;

import gift.dto.CreateMemberRequestDto;
import gift.dto.CreateProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.service.MemberService;
import gift.service.ProductService;
import gift.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/boards")
public class AdminProductController {

    private final ProductService productService;

    private final MemberService memberService;

    private final String BOARD_PAGE = "/admin/boards";

    public AdminProductController(ProductService productService,
            MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
    }

    @GetMapping
    public String showAdminPage(Model model) {
        List<ProductResponseDto> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "dashboard";
    }

    @GetMapping("/add")
    public String showCreatePage(Model model) {
        return "createForm";
    }

    @GetMapping("/update/{id}")
    public String showUpdatePage(
            @PathVariable Long id,
            Model model) {
        model.addAttribute("id", id);
        return "updateForm";
    }

    @PostMapping
    public String createProduct(
            @Valid @ModelAttribute CreateProductRequestDto requestDto) {
        if (requestDto.name().contains("카카오")) {
            throw new CustomException(ErrorCode.NamingForbidden);
        }
        productService.createProduct(requestDto);
        return "redirect:" + BOARD_PAGE;
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id,
            @Valid @ModelAttribute CreateProductRequestDto requestDto) {
        if (requestDto.name().contains("카카오")) {
            throw new CustomException(ErrorCode.NamingForbidden);
        }
        productService.updateProductById(id, requestDto);
        return "redirect:" + BOARD_PAGE;
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(
            @PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:" + BOARD_PAGE;
    }

}
