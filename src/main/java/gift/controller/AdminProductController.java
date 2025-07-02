package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    // 목록
    @GetMapping
    public String showProductList(Model model) {
        List<ProductResponseDto> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product/list";
    }

    // 상품 등록
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductRequestDto("", 0, ""));
        model.addAttribute("isEdit", false);
        return "product/form";
    }

    // 등록 처리
    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") ProductRequestDto requestDto,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "product/form";
        }
        productService.saveProduct(requestDto);
        return "redirect:/admin/products";
    }

    // 상품 수정
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProductResponseDto product = productService.findProductById(id);
        model.addAttribute("product",
                new ProductRequestDto(product.name(), product.price(), product.imageUrl()));
        model.addAttribute("productId", product.id());
        model.addAttribute("isEdit", true);
        return "product/form";
    }

    // 수정 처리
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
            @Valid @ModelAttribute("product") ProductRequestDto requestDto,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("productId", id);
            return "product/form";
        }
        productService.updateProduct(id, requestDto.name(), requestDto.price(),
                requestDto.imageUrl());
        return "redirect:/admin/products";
    }

    // 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
