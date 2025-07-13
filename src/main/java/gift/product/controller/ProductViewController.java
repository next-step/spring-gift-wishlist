package gift.product.controller;

import gift.common.pagination.PageRequestDto;
import gift.common.pagination.PageResult;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 등록
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new ProductRequestDto("하리보 콜라맛", 2000, "test.jpg"));
        return "product/create";
    }

    // 상품 등록 처리
    @PostMapping
    public String create(@ModelAttribute("product") @Valid ProductRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product/create";
        }

        productService.createProduct(dto);
        return "redirect:/admin/products";
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       Model model) {

        PageRequestDto pageRequestDto = new PageRequestDto(page, size);
        PageResult<ProductResponseDto> pageResult = productService.findAllProducts(pageRequestDto);

        model.addAttribute("pageResult", pageResult);
        return "product/list";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Long id, Model model) {
        ProductResponseDto product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "product/detail";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        ProductResponseDto product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "product/update";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("product") @Valid ProductRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product/update";
        }

        productService.updateProduct(id, dto);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

}
