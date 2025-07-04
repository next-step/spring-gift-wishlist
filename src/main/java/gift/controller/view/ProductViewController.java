package gift.controller.view;

import gift.model.CustomPage;
import gift.entity.Product;
import gift.service.product.ProductService;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/products")
public class ProductViewController {
    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProductList(
        Model model,
        @RequestParam(value = "page", defaultValue = "0")
        @Min(value = 0, message = "페이지 번호는 0 이상이여야 합니다.") Integer page,
        @RequestParam(value = "size", defaultValue = "5")
        @Min(value = 1, message = "페이지 크기는 양수여야 합니다.") Integer size
    ) {
        CustomPage<Product> currentPage = productService.getBy(page, size);
        model.addAttribute("title", "관리자 상품 목록");
        model.addAttribute("pageInfo", currentPage);

        return "admin/product-list";
    }

    @GetMapping("/{id}")
    public String showProductDetails(
            @PathVariable @Min(value = 0, message = "상품 ID는 0 이상이어야 합니다.") Long id,
            Model model
    ) {
        Product product = productService.getById(id);

        model.addAttribute("title", "상품 상세 정보");
        model.addAttribute("product", product);
        return "admin/view-product";
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("title", "상품 등록");
        return "admin/create-product";
    }
}
