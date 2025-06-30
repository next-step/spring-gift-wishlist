package gift.controller;

import gift.dto.response.ProductResponse;
import gift.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    // 목록 조회 화면
    @GetMapping
    public String showList() {
        return "admin/product-list";
    }

    // 상품 등록
    @GetMapping("/new")
    public String showCreateForm() {
        return "admin/product-register";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProductResponse product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "admin/product-edit";
    }
}