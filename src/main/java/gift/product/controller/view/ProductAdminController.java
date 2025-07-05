package gift.product.controller.view;

import gift.product.domain.Product;
import gift.product.dto.RequestDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/api/admin")
public class ProductAdminController {
    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/list")
    public String findAll(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/product/add")
    public String addForm(Model model) {
        model.addAttribute("requestDto", new RequestDto());
        return "addForm";
    }

    @PostMapping("/product/add")
    public String saveProduct(@Valid @ModelAttribute RequestDto requestDto) {
        productService.saveProduct(requestDto);
        return "redirect:/api/admin/product/list";
    }

    @ResponseBody
    @GetMapping("/product/{id}")
    public Product findById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    @GetMapping("/product/{id}/update")
    public String updateForm(@PathVariable UUID id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "updateForm";
    }

    @PatchMapping("/product/{id}/update")
    public String updateProduct(@PathVariable UUID id, @Valid @ModelAttribute RequestDto requestDto) {
        productService.updateProduct(id, requestDto);
        return "redirect:/api/admin/product/list";
    }

    @DeleteMapping("/product/{id}/delete")
    public String deleteById(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return "redirect:/api/admin/product/list";
    }
}
