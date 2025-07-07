package gift.controller;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProductList(Model model) {
        List<ProductResponse> products = productService.findAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("productRequest", new ProductRequest("", 0, ""));
        return "admin/product-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest("", 0, ""));
        return "admin/product-form";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("productRequest") ProductRequest productRequest,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시, 오류 정보와 함께 목록 페이지를 다시 보여줍니다.
            List<ProductResponse> products = productService.findAllProducts();
            model.addAttribute("products", products);
            return "admin/product-list";
        }
        productService.addProduct(productRequest);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        ProductResponse product = productService.findProductById(id);
        model.addAttribute("productRequest", new ProductRequest(product.name(), product.price(), product.imageUrl()));
        model.addAttribute("productId", id);
        return "admin/product-edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("productRequest") ProductRequest productRequest,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "admin/product-edit-form";
        }
        productService.updateProduct(id, productRequest);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete")
    public String deleteSelectedProducts(@RequestParam(value = "productIds", required = false) List<Long> productIds) {
        if (productIds != null && !productIds.isEmpty()) {
            productService.deleteProducts(productIds);
        }
        return "redirect:/admin/products";
    }
}